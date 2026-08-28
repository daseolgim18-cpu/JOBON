package com.jobon.social.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * Google/Naver/Kakao SNS 로그인 시작 및 Callback Controller입니다.
 * OAuth state 값을 세션에 저장하여 요청 위조를 방지하고,
 * SNS 인증 완료 후 JOBON 로그인 세션을 생성합니다.
 */
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobon.member.vo.MemberVO;
import com.jobon.social.service.SocialLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SocialLoginController {
    private final SocialLoginService socialLoginService;

    public SocialLoginController(SocialLoginService socialLoginService) {
        this.socialLoginService = socialLoginService;
    }

    // SNS 로그인 시작: CSRF 방지를 위한 state 값을 만든 뒤 각 SNS 인증 페이지로 보낸다.
    @GetMapping("/member/{provider:google|naver|kakao}/login")
    public String start(@PathVariable String provider, HttpSession session, RedirectAttributes ra) {
        try {
            String state = UUID.randomUUID().toString();
            session.setAttribute("oauthState_" + provider, state);

            // [추가] 일반 로그인 시작 시 이전 연동 시도 정보가 남아 있지 않도록 초기화합니다.
            session.removeAttribute("oauthMode_" + provider);
            session.removeAttribute("oauthLinkMemberId_" + provider);

            return "redirect:" + socialLoginService.createLoginUrl(provider, state);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

    // [추가] 마이페이지 SNS 연동 시작: 현재 로그인 회원을 세션에 기록하고 기존 SNS OAuth 인증 화면으로 이동합니다.
    // 별도 Callback URI를 추가하지 않고 기존 /member/{provider}/callback을 재사용합니다.
    @GetMapping("/mypage/accounts/{provider:google|naver|kakao}/link")
    public String startLink(@PathVariable String provider, HttpSession session, RedirectAttributes ra) {
        Long memberId = getLoginMemberId(session);
        if (memberId == null) {
            ra.addFlashAttribute("errorMessage", "로그인이 필요한 서비스입니다.");
            return "redirect:/login";
        }

        try {
            String state = UUID.randomUUID().toString();
            session.setAttribute("oauthState_" + provider, state);
            session.setAttribute("oauthMode_" + provider, "LINK");
            session.setAttribute("oauthLinkMemberId_" + provider, memberId);
            return "redirect:" + socialLoginService.createLoginUrl(provider, state);
        } catch (RuntimeException e) {
            session.removeAttribute("oauthMode_" + provider);
            session.removeAttribute("oauthLinkMemberId_" + provider);
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mypage/accounts";
        }
    }

    // [추가] 마이페이지 SNS 연동 해제: SOCIAL_ACCOUNT 연결만 삭제하고 JOBON 회원 자체는 유지합니다.
    @PostMapping("/mypage/accounts/{provider:google|naver|kakao}/unlink")
    public String unlink(@PathVariable String provider, HttpSession session, RedirectAttributes ra) {
        Long memberId = getLoginMemberId(session);
        if (memberId == null) {
            ra.addFlashAttribute("errorMessage", "로그인이 필요한 서비스입니다.");
            return "redirect:/login";
        }

        try {
            socialLoginService.unlinkAccount(memberId, provider);
            ra.addFlashAttribute("successMessage", providerName(provider) + " 계정 연동을 해제했습니다.");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/mypage/accounts";
    }

    // SNS Callback: state 검증 → 사용자 인증 → JOBON 세션 생성 순으로 처리한다.
    @GetMapping("/member/{provider:google|naver|kakao}/callback")
    public String callback(@PathVariable String provider,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String error,
            HttpServletRequest request, HttpSession session, RedirectAttributes ra) {
        String expected = (String) session.getAttribute("oauthState_" + provider);
        String oauthMode = (String) session.getAttribute("oauthMode_" + provider);
        Object linkMemberIdValue = session.getAttribute("oauthLinkMemberId_" + provider);

        session.removeAttribute("oauthState_" + provider);
        session.removeAttribute("oauthMode_" + provider);
        session.removeAttribute("oauthLinkMemberId_" + provider);

        boolean linkMode = "LINK".equals(oauthMode);
        String failRedirect = linkMode ? "redirect:/mypage/accounts" : "redirect:/login";

        // 저장해 둔 state와 Callback의 state가 다르면 위조/잘못된 요청으로 판단한다.
        if (error != null || expected == null || state == null || !expected.equals(state)) {
            ra.addFlashAttribute("errorMessage", "SNS 로그인 요청 검증에 실패했습니다. 다시 시도해주세요.");
            return failRedirect;
        }
        try {
            // [추가] 마이페이지에서 시작한 OAuth 요청은 새 회원 생성/로그인 대신 현재 회원에게 SNS를 연결합니다.
            if (linkMode) {
                Long currentMemberId = getLoginMemberId(session);
                Long linkMemberId = linkMemberIdValue instanceof Number number ? number.longValue() : null;
                if (currentMemberId == null || linkMemberId == null || !currentMemberId.equals(linkMemberId)) {
                    throw new IllegalStateException("연동 요청의 로그인 회원 정보가 일치하지 않습니다. 다시 시도해주세요.");
                }

                socialLoginService.linkAccount(currentMemberId, provider, code, state);
                ra.addFlashAttribute("successMessage", providerName(provider) + " 계정이 연동되었습니다.");
                return "redirect:/mypage/accounts";
            }

            MemberVO member = socialLoginService.login(provider, code, state);
            request.changeSessionId();
            session.setAttribute("loginMember", member);
            session.setAttribute("loginMemberId", member.getMemberId());
            session.setAttribute("loginProvider", provider.toUpperCase());
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return failRedirect;
        }
    }

    // [추가] 일반 로그인과 SNS 로그인에서 공통으로 저장하는 MEMBER_ID를 안전하게 조회합니다.
    private Long getLoginMemberId(HttpSession session) {
        Object memberIdValue = session.getAttribute("loginMemberId");
        if (memberIdValue == null && session.getAttribute("loginMember") instanceof MemberVO member) {
            memberIdValue = member.getMemberId();
        }
        return memberIdValue instanceof Number number ? number.longValue() : null;
    }

    // [추가] 사용자 안내 메시지에는 영문 enum 값 대신 서비스 이름을 표시합니다.
    private String providerName(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> "Google";
            case "naver" -> "Naver";
            case "kakao" -> "Kakao";
            default -> provider;
        };
    }

}