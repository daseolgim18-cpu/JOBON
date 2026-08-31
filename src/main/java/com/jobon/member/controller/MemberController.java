package com.jobon.member.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 회원 관련 JSP 화면 이동 Controller입니다.
 * 로그인, 회원가입, 가입완료, 비밀번호 찾기 화면을 반환합니다.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping({ "/join", "/member/join" })
    public String join() {
        return "member/join";
    }

    @GetMapping("/member/join/complete")
    public String joinComplete() {
        return "member/join-complete";
    }

    @GetMapping("/member/find-password")
    public String findPassword() {
        return "member/find-password";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }
}
