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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobon.member.vo.MemberVO;
import com.jobon.social.service.SocialLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class SocialLoginController {
    private final SocialLoginService socialLoginService;
    public SocialLoginController(SocialLoginService socialLoginService) { this.socialLoginService = socialLoginService; }

    // SNS 로그인 시작: CSRF 방지를 위한 state 값을 만든 뒤 각 SNS 인증 페이지로 보낸다.
    @GetMapping("/member/{provider:google|naver|kakao}/login")
    public String start(@PathVariable String provider, HttpSession session, RedirectAttributes ra) {
        try {
            String state = UUID.randomUUID().toString();
            session.setAttribute("oauthState_" + provider, state);
            return "redirect:" + socialLoginService.createLoginUrl(provider, state);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }

    // SNS Callback: state 검증 → 사용자 인증 → JOBON 세션 생성 순으로 처리한다.
    @GetMapping("/member/{provider:google|naver|kakao}/callback")
    public String callback(@PathVariable String provider,
                           @RequestParam(required = false) String code,
                           @RequestParam(required = false) String state,
                           @RequestParam(required = false) String error,
                           HttpServletRequest request, HttpSession session, RedirectAttributes ra) {
        String expected = (String) session.getAttribute("oauthState_" + provider);
        session.removeAttribute("oauthState_" + provider);
        // 저장해 둔 state와 Callback의 state가 다르면 위조/잘못된 요청으로 판단한다.
        if (error != null || expected == null || state == null || !expected.equals(state)) {
            ra.addFlashAttribute("errorMessage", "SNS 로그인 요청 검증에 실패했습니다. 다시 시도해주세요.");
            return "redirect:/login";
        }
        try {
            MemberVO member = socialLoginService.login(provider, code, state);
            request.changeSessionId();
            session.setAttribute("loginMember", member);
            session.setAttribute("loginMemberId", member.getMemberId());
            session.setAttribute("loginProvider", provider.toUpperCase());
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login";
        }
    }
}
