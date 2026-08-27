package com.jobon.social.service;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Google/Naver/Kakao OAuth 로그인 핵심 로직입니다.
 * 인가코드 → Access Token → 사용자 정보 조회 → SOCIAL_ACCOUNT 연결 → JOBON 회원 로그인 순서로 처리합니다.
 */
import java.net.URI;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.jobon.member.service.MemberService;
import com.jobon.member.vo.MemberVO;
import com.jobon.social.dao.SocialAccountDAO;
import com.jobon.social.vo.SocialAccountVO;
import com.jobon.social.vo.SocialProfile;

@Service
public class SocialLoginServiceImpl implements SocialLoginService {
    private final MemberService memberService;
    private final SocialAccountDAO socialAccountDAO;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${jobon.oauth.google.client-id:}") private String googleClientId;
    @Value("${jobon.oauth.google.client-secret:}") private String googleClientSecret;
    @Value("${jobon.oauth.google.redirect-uri:http://localhost:8080/member/google/callback}") private String googleRedirectUri;
    @Value("${jobon.oauth.naver.client-id:}") private String naverClientId;
    @Value("${jobon.oauth.naver.client-secret:}") private String naverClientSecret;
    @Value("${jobon.oauth.naver.redirect-uri:http://localhost:8080/member/naver/callback}") private String naverRedirectUri;
    @Value("${jobon.oauth.kakao.client-id:}") private String kakaoClientId;
    @Value("${jobon.oauth.kakao.client-secret:}") private String kakaoClientSecret;
    @Value("${jobon.oauth.kakao.redirect-uri:http://localhost:8080/member/kakao/callback}") private String kakaoRedirectUri;

    public SocialLoginServiceImpl(MemberService memberService, SocialAccountDAO socialAccountDAO) {
        this.memberService = memberService;
        this.socialAccountDAO = socialAccountDAO;
    }

    @Override
    // 공급자별 OAuth 인증 URL을 생성한다. Client ID, redirect_uri, state를 함께 전달한다.
    public String createLoginUrl(String provider, String state) {
        provider = provider.toUpperCase();
        return switch (provider) {
            case "GOOGLE" -> UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
                    .queryParam("client_id", required(googleClientId, "Google client-id"))
                    .queryParam("redirect_uri", googleRedirectUri).queryParam("response_type", "code")
                    .queryParam("scope", "openid email profile").queryParam("state", state).build().encode().toUriString();
            case "NAVER" -> UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                    .queryParam("response_type", "code").queryParam("client_id", required(naverClientId, "Naver client-id"))
                    .queryParam("redirect_uri", naverRedirectUri).queryParam("state", state).build().encode().toUriString();
            case "KAKAO" -> UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("response_type", "code").queryParam("client_id", required(kakaoClientId, "Kakao client-id"))
                    .queryParam("redirect_uri", kakaoRedirectUri).queryParam("state", state).build().encode().toUriString();
            default -> throw new IllegalArgumentException("지원하지 않는 SNS입니다.");
        };
    }

    @Override
    @Transactional
    // 공통 SNS 로그인 흐름: 사용자 프로필 조회 → SOCIAL_ACCOUNT 조회/등록 → JOBON 회원 반환
    public MemberVO login(String provider, String code, String state) {
        if (code == null || code.isBlank()) throw new IllegalArgumentException("SNS 인증 코드가 없습니다.");
        SocialProfile profile = switch (provider.toUpperCase()) {
            case "GOOGLE" -> googleProfile(code);
            case "NAVER" -> naverProfile(code, state);
            case "KAKAO" -> kakaoProfile(code);
            default -> throw new IllegalArgumentException("지원하지 않는 SNS입니다.");
        };

        // 이미 연동된 SNS 계정인지 먼저 확인한다.
        SocialAccountVO social = socialAccountDAO.selectByProvider(profile.provider(), profile.providerUserId());
        MemberVO member;
        if (social != null) {
            member = memberService.findById(social.getMemberId());
            if (member == null) throw new IllegalStateException("연동된 회원 정보를 찾을 수 없습니다.");
            socialAccountDAO.updateLastLoginAt(social.getSocialAccountId());
        } else {
            member = memberService.createSocialMember(profile.provider(), profile.providerUserId(), profile.email(), profile.name(), profile.nickname(), profile.profileImageUrl());
            SocialAccountVO account = new SocialAccountVO();
            account.setMemberId(member.getMemberId());
            account.setProvider(profile.provider());
            account.setProviderUserId(profile.providerUserId());
            account.setEmail(profile.email());
            socialAccountDAO.insertSocialAccount(account);
        }
        if (!"ACTIVE".equalsIgnoreCase(member.getStatus())) throw new IllegalStateException("현재 로그인할 수 없는 계정입니다.");
        memberService.touchLastLogin(member.getMemberId());
        return memberService.findById(member.getMemberId());
    }

    @SuppressWarnings("unchecked")
    // Google 인가코드로 Access Token을 발급받고 Google 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile googleProfile(String code) {
        Map<String,Object> token = token("https://oauth2.googleapis.com/token", params(
                "grant_type","authorization_code","client_id",required(googleClientId,"Google client-id"),
                "client_secret",required(googleClientSecret,"Google client-secret"),"redirect_uri",googleRedirectUri,"code",code));
        Map<String,Object> user = getJson("https://www.googleapis.com/oauth2/v3/userinfo", tokenString(token));
        return new SocialProfile("GOOGLE", string(user.get("sub")), string(user.get("email")), string(user.get("name")), string(user.get("name")), string(user.get("picture")));
    }

    @SuppressWarnings("unchecked")
    // Naver 인가코드로 Access Token을 발급받고 Naver 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile naverProfile(String code, String state) {
        String tokenUrl = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type","authorization_code").queryParam("client_id",required(naverClientId,"Naver client-id"))
                .queryParam("client_secret",required(naverClientSecret,"Naver client-secret")).queryParam("code",code).queryParam("state",state).build().encode().toUriString();
        Map<String,Object> token = restTemplate.getForObject(tokenUrl, Map.class);
        Map<String,Object> root = getJson("https://openapi.naver.com/v1/nid/me", tokenString(token));
        Map<String,Object> user = (Map<String,Object>) root.get("response");
        if (user == null) throw new IllegalStateException("Naver 사용자 정보를 확인할 수 없습니다.");
        return new SocialProfile("NAVER", string(user.get("id")), string(user.get("email")), string(user.get("name")), string(user.get("nickname")), string(user.get("profile_image")));
    }

    @SuppressWarnings("unchecked")
    // Kakao 인가코드로 Access Token을 발급받고 Kakao 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile kakaoProfile(String code) {
        MultiValueMap<String,String> p = params("grant_type","authorization_code","client_id",required(kakaoClientId,"Kakao client-id"),"redirect_uri",kakaoRedirectUri,"code",code);
        if (kakaoClientSecret != null && !kakaoClientSecret.isBlank()) p.add("client_secret", kakaoClientSecret);
        Map<String,Object> token = token("https://kauth.kakao.com/oauth/token", p);
        Map<String,Object> root = getJson("https://kapi.kakao.com/v2/user/me", tokenString(token));
        Map<String,Object> account = (Map<String,Object>) root.get("kakao_account");
        Map<String,Object> properties = (Map<String,Object>) root.get("properties");
        Map<String,Object> profile = account == null ? null : (Map<String,Object>) account.get("profile");
        String nickname = profile != null ? string(profile.get("nickname")) : properties == null ? null : string(properties.get("nickname"));
        String image = profile != null ? string(profile.get("profile_image_url")) : properties == null ? null : string(properties.get("profile_image"));
        String email = account == null ? null : string(account.get("email"));
        return new SocialProfile("KAKAO", string(root.get("id")), email, nickname, nickname, image);
    }

    @SuppressWarnings("unchecked")
    // OAuth Token Endpoint에 POST 요청하여 access_token 응답을 받는 공통 메서드
    private Map<String,Object> token(String url, MultiValueMap<String,String> params) {
        HttpHeaders h = new HttpHeaders(); h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<Map> r = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(params,h), Map.class);
        return (Map<String,Object>) r.getBody();
    }

    @SuppressWarnings("unchecked")
    // 발급받은 Bearer Access Token으로 SNS 사용자 정보 API를 호출하는 공통 메서드
    private Map<String,Object> getJson(String url, String accessToken) {
        HttpHeaders h = new HttpHeaders(); h.setBearerAuth(accessToken);
        ResponseEntity<Map> r = restTemplate.exchange(URI.create(url), HttpMethod.GET, new HttpEntity<>(h), Map.class);
        if (r.getBody() == null) throw new IllegalStateException("SNS 사용자 정보를 불러오지 못했습니다.");
        return (Map<String,Object>) r.getBody();
    }

    private String tokenString(Map<String,Object> token) {
        if (token == null || token.get("access_token") == null) throw new IllegalStateException("SNS access token 발급에 실패했습니다.");
        return String.valueOf(token.get("access_token"));
    }
    private MultiValueMap<String,String> params(String... pairs) { MultiValueMap<String,String> p = new LinkedMultiValueMap<>(); for(int i=0;i<pairs.length;i+=2)p.add(pairs[i],pairs[i+1]); return p; }
    private String required(String value, String name) { if(value==null||value.isBlank()) throw new IllegalStateException(name+" 설정이 없습니다."); return value; }
    private String string(Object value) { return value == null ? null : String.valueOf(value); }
}
