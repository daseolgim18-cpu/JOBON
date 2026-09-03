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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobon.member.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

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

    @GetMapping("/member/find-id")
    public String findId() {
        return "member/find-id";
    }

    @PostMapping("/member/find-id")
    public String findIdProcess(@RequestParam String name, @RequestParam String email, @RequestParam String phone,
            RedirectAttributes redirectAttributes) {
        try {
            String loginId = memberService.findLoginId(name, email, phone);
            redirectAttributes.addFlashAttribute("foundLoginId", loginId);
            redirectAttributes.addFlashAttribute("findIdSuccess", true);
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/member/find-id";
    }

    @GetMapping("/member/find-password")
    public String findPassword() {
        return "member/find-password";
    }

    @PostMapping("/member/find-password")
    public String findPasswordProcess(@RequestParam String loginId, @RequestParam String name,
            @RequestParam String email,
            RedirectAttributes redirectAttributes) {
        try {
            memberService.issueTemporaryPassword(loginId, name, email);
            redirectAttributes.addFlashAttribute("successMessage",
                    "가입한 이메일로 임시 비밀번호를 발송했습니다. 로그인 후 비밀번호를 변경해주세요.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (RuntimeException e) {
            // [수정] Gmail SMTP 발송 실패 원인을 서버 콘솔에서 확인할 수 있도록 로그를 출력합니다.
            e.printStackTrace();

            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "메일 발송에 실패했습니다. 서버 로그를 확인해주세요.");
        }
        return "redirect:/member/find-password";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/main";
    }
}
