package com.jobon.social.service;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Google/Naver/Kakao OAuth 로그인 핵심 로직입니다.
 * 인가코드 → Access Token → 사용자 정보 조회 → SOCIAL_ACCOUNT 연결 → JOBON 회원 로그인 순서로 처리합니다.
 */
import java.net.URI;
import java.util.List;
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

    @Value("${jobon.oauth.google.client-id:}")
    private String googleClientId;
    @Value("${jobon.oauth.google.client-secret:}")
    private String googleClientSecret;
    @Value("${jobon.oauth.google.redirect-uri:http://localhost:8080/member/google/callback}")
    private String googleRedirectUri;
    @Value("${jobon.oauth.naver.client-id:}")
    private String naverClientId;
    @Value("${jobon.oauth.naver.client-secret:}")
    private String naverClientSecret;
    @Value("${jobon.oauth.naver.redirect-uri:http://localhost:8080/member/naver/callback}")
    private String naverRedirectUri;
    // [수정] 기존 application.properties의 underscore 키와 표준 hyphen 키를 모두 지원합니다.
    @Value("${jobon.oauth.kakao.client-id:${jobon.oauth.kakao.client_id:}}")
    private String kakaoClientId;
    @Value("${jobon.oauth.kakao.client-secret:${jobon.oauth.kakao.client_secret:}}")
    private String kakaoClientSecret;
    @Value("${jobon.oauth.kakao.redirect-uri:${jobon.oauth.kakao.redirect_uri:http://localhost:8080/jobon/member/kakao/callback}}")
    private String kakaoRedirectUri;

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
                    .queryParam("scope", "openid email profile").queryParam("state", state).build().encode()
                    .toUriString();
            case "NAVER" -> UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/authorize")
                    .queryParam("response_type", "code")
                    .queryParam("client_id", required(naverClientId, "Naver client-id"))
                    .queryParam("redirect_uri", naverRedirectUri).queryParam("state", state).build().encode()
                    .toUriString();
            case "KAKAO" -> UriComponentsBuilder.fromUriString("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("response_type", "code")
                    .queryParam("client_id", required(kakaoClientId, "Kakao client-id"))
                    .queryParam("redirect_uri", kakaoRedirectUri).queryParam("state", state).build().encode()
                    .toUriString();
            default -> throw new IllegalArgumentException("지원하지 않는 SNS입니다.");
        };
    }

    @Override
    @Transactional
    // 공통 SNS 로그인 흐름: 사용자 프로필 조회 → SOCIAL_ACCOUNT 조회/등록 → JOBON 회원 반환
    public MemberVO login(String provider, String code, String state) {
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("SNS 인증 코드가 없습니다.");
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
            if (member == null)
                throw new IllegalStateException("연동된 회원 정보를 찾을 수 없습니다.");
            socialAccountDAO.updateLastLoginAt(social.getSocialAccountId());
        } else {
            // [추가] 사용자가 마이페이지에서 연동 해제한 SNS가 일반 로그인만으로 다시 자동 연동되지 않도록,
            // 동일 이메일의 기존 JOBON 회원이 있으면 로그인 화면에서 자동 연결하지 않고 마이페이지 연동을 안내합니다.
            if (profile.email() != null && memberService.findByEmail(profile.email()) != null) {
                throw new IllegalStateException("이미 가입된 이메일입니다. 기존 계정으로 로그인한 후 마이페이지에서 "
                        + profile.provider() + " 계정을 연동해주세요.");
            }

            member = memberService.createSocialMember(profile.provider(), profile.providerUserId(), profile.email(),
                    profile.name(), profile.nickname(), profile.profileImageUrl());
            SocialAccountVO account = new SocialAccountVO();
            account.setMemberId(member.getMemberId());
            account.setProvider(profile.provider());
            account.setProviderUserId(profile.providerUserId());
            account.setEmail(profile.email());
            socialAccountDAO.insertSocialAccount(account);
        }
        if (!"ACTIVE".equalsIgnoreCase(member.getStatus()))
            throw new IllegalStateException("현재 로그인할 수 없는 계정입니다.");
        memberService.touchLastLogin(member.getMemberId());
        return memberService.findById(member.getMemberId());
    }

    @Override
    @Transactional
    // [추가] 로그인된 JOBON 회원에게 SNS 계정을 연결합니다.
    // 일반 SNS 로그인과 달리 신규 JOBON_MEMBER를 만들지 않고 현재 MEMBER_ID에 SOCIAL_ACCOUNT만 추가합니다.
    public void linkAccount(Long memberId, String provider, String code, String state) {
        if (memberId == null)
            throw new IllegalArgumentException("로그인 회원 정보를 확인할 수 없습니다.");
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("SNS 인증 코드가 없습니다.");

        MemberVO member = memberService.findById(memberId);
        if (member == null || !"ACTIVE".equalsIgnoreCase(member.getStatus())) {
            throw new IllegalStateException("현재 연동할 수 없는 회원입니다.");
        }

        SocialProfile profile = switch (provider.toUpperCase()) {
            case "GOOGLE" -> googleProfile(code);
            case "NAVER" -> naverProfile(code, state);
            case "KAKAO" -> kakaoProfile(code);
            default -> throw new IllegalArgumentException("지원하지 않는 SNS입니다.");
        };

        // [추가] 같은 SNS 계정이 이미 다른 JOBON 회원에게 연결되어 있으면 계정 탈취/중복 연동을 막습니다.
        SocialAccountVO linkedAccount = socialAccountDAO.selectByProvider(profile.provider(), profile.providerUserId());
        if (linkedAccount != null) {
            if (memberId.equals(linkedAccount.getMemberId())) {
                throw new IllegalStateException(profile.provider() + " 계정이 이미 연동되어 있습니다.");
            }
            throw new IllegalStateException("해당 " + profile.provider() + " 계정은 다른 JOBON 회원에 이미 연동되어 있습니다.");
        }

        // [추가] 한 JOBON 회원에게 같은 제공자의 서로 다른 계정이 여러 개 연결되지 않도록 제한합니다.
        SocialAccountVO sameProvider = socialAccountDAO.selectByMemberAndProvider(memberId, profile.provider());
        if (sameProvider != null) {
            throw new IllegalStateException(profile.provider() + " 계정이 이미 연동되어 있습니다. 기존 연동을 해제한 후 다시 시도해주세요.");
        }

        SocialAccountVO account = new SocialAccountVO();
        account.setMemberId(memberId);
        account.setProvider(profile.provider());
        account.setProviderUserId(profile.providerUserId());
        account.setEmail(profile.email());
        if (socialAccountDAO.insertSocialAccount(account) != 1) {
            throw new IllegalStateException("SNS 계정을 연동하지 못했습니다.");
        }
    }

    @Override
    @Transactional
    // [추가] SNS 연동 해제는 SOCIAL_ACCOUNT만 삭제합니다.
    // 비밀번호가 없는 SNS 전용 회원은 로그인 수단이 하나만 남은 경우 마지막 SNS 연동을 해제할 수 없습니다.
    public void unlinkAccount(Long memberId, String provider) {
        if (memberId == null)
            throw new IllegalArgumentException("로그인 회원 정보를 확인할 수 없습니다.");
        String normalizedProvider = provider == null ? "" : provider.toUpperCase();
        if (!("GOOGLE".equals(normalizedProvider) || "NAVER".equals(normalizedProvider)
                || "KAKAO".equals(normalizedProvider))) {
            throw new IllegalArgumentException("지원하지 않는 SNS입니다.");
        }

        MemberVO member = memberService.findById(memberId);
        if (member == null)
            throw new IllegalStateException("회원 정보를 찾을 수 없습니다.");

        SocialAccountVO account = socialAccountDAO.selectByMemberAndProvider(memberId, normalizedProvider);
        if (account == null)
            throw new IllegalStateException("연동되어 있지 않은 SNS 계정입니다.");

        boolean hasPassword = member.getPasswordHash() != null && !member.getPasswordHash().isBlank();
        int linkedCount = socialAccountDAO.countByMemberId(memberId);
        if (!hasPassword && linkedCount <= 1) {
            throw new IllegalStateException("현재 유일한 로그인 수단입니다. 다른 SNS 계정을 먼저 연동한 후 해제해주세요.");
        }

        if (socialAccountDAO.deleteByMemberAndProvider(memberId, normalizedProvider) != 1) {
            throw new IllegalStateException("SNS 계정 연동을 해제하지 못했습니다.");
        }
    }

    @Override
    // [추가] 현재 회원의 SNS 연동 목록을 마이페이지에 전달합니다.
    public List<SocialAccountVO> findLinkedAccounts(Long memberId) {
        if (memberId == null)
            throw new IllegalArgumentException("로그인 회원 정보를 확인할 수 없습니다.");
        return socialAccountDAO.selectByMemberId(memberId);
    }

    @SuppressWarnings("unchecked")
    // Google 인가코드로 Access Token을 발급받고 Google 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile googleProfile(String code) {
        Map<String, Object> token = token("https://oauth2.googleapis.com/token", params(
                "grant_type", "authorization_code", "client_id", required(googleClientId, "Google client-id"),
                "client_secret", required(googleClientSecret, "Google client-secret"), "redirect_uri",
                googleRedirectUri, "code", code));
        Map<String, Object> user = getJson("https://www.googleapis.com/oauth2/v3/userinfo", tokenString(token));
        return new SocialProfile("GOOGLE", string(user.get("sub")), string(user.get("email")), string(user.get("name")),
                string(user.get("name")), string(user.get("picture")));
    }

    @SuppressWarnings("unchecked")
    // Naver 인가코드로 Access Token을 발급받고 Naver 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile naverProfile(String code, String state) {
        String tokenUrl = UriComponentsBuilder.fromUriString("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", required(naverClientId, "Naver client-id"))
                .queryParam("client_secret", required(naverClientSecret, "Naver client-secret"))
                .queryParam("code", code).queryParam("state", state).build().encode().toUriString();
        Map<String, Object> token = restTemplate.getForObject(tokenUrl, Map.class);
        Map<String, Object> root = getJson("https://openapi.naver.com/v1/nid/me", tokenString(token));
        Map<String, Object> user = (Map<String, Object>) root.get("response");
        if (user == null)
            throw new IllegalStateException("Naver 사용자 정보를 확인할 수 없습니다.");
        return new SocialProfile("NAVER", string(user.get("id")), string(user.get("email")), string(user.get("name")),
                string(user.get("nickname")), string(user.get("profile_image")));
    }

    @SuppressWarnings("unchecked")
    // Kakao 인가코드로 Access Token을 발급받고 Kakao 사용자 정보를 공통 SocialProfile로 변환한다.
    private SocialProfile kakaoProfile(String code) {
        MultiValueMap<String, String> p = params("grant_type", "authorization_code", "client_id",
                required(kakaoClientId, "Kakao client-id"), "redirect_uri", kakaoRedirectUri, "code", code);
        if (kakaoClientSecret != null && !kakaoClientSecret.isBlank())
            p.add("client_secret", kakaoClientSecret);
        Map<String, Object> token = token("https://kauth.kakao.com/oauth/token", p);
        Map<String, Object> root = getJson("https://kapi.kakao.com/v2/user/me", tokenString(token));
        Map<String, Object> account = (Map<String, Object>) root.get("kakao_account");
        Map<String, Object> properties = (Map<String, Object>) root.get("properties");
        Map<String, Object> profile = account == null ? null : (Map<String, Object>) account.get("profile");
        String nickname = profile != null ? string(profile.get("nickname"))
                : properties == null ? null : string(properties.get("nickname"));
        String image = profile != null ? string(profile.get("profile_image_url"))
                : properties == null ? null : string(properties.get("profile_image"));
        String email = account == null ? null : string(account.get("email"));
        return new SocialProfile("KAKAO", string(root.get("id")), email, nickname, nickname, image);
    }

    @SuppressWarnings("unchecked")
    // OAuth Token Endpoint에 POST 요청하여 access_token 응답을 받는 공통 메서드
    private Map<String, Object> token(String url, MultiValueMap<String, String> params) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<Map> r = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(params, h), Map.class);
        return (Map<String, Object>) r.getBody();
    }

    @SuppressWarnings("unchecked")
    // 발급받은 Bearer Access Token으로 SNS 사용자 정보 API를 호출하는 공통 메서드
    private Map<String, Object> getJson(String url, String accessToken) {
        HttpHeaders h = new HttpHeaders();
        h.setBearerAuth(accessToken);
        ResponseEntity<Map> r = restTemplate.exchange(URI.create(url), HttpMethod.GET, new HttpEntity<>(h), Map.class);
        if (r.getBody() == null)
            throw new IllegalStateException("SNS 사용자 정보를 불러오지 못했습니다.");
        return (Map<String, Object>) r.getBody();
    }

    private String tokenString(Map<String, Object> token) {
        if (token == null || token.get("access_token") == null)
            throw new IllegalStateException("SNS access token 발급에 실패했습니다.");
        return String.valueOf(token.get("access_token"));
    }

    private MultiValueMap<String, String> params(String... pairs) {
        MultiValueMap<String, String> p = new LinkedMultiValueMap<>();
        for (int i = 0; i < pairs.length; i += 2)
            p.add(pairs[i], pairs[i + 1]);
        return p;
    }

    private String required(String value, String name) {
        if (value == null || value.isBlank())
            throw new IllegalStateException(name + " 설정이 없습니다.");
        return value;
    }

    private String string(Object value) {
        return value == null ? null : String.valueOf(value);
    }
}
