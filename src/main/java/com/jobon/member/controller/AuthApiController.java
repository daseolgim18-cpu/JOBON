package com.jobon.member.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 일반 로그인/로그아웃 REST API Controller입니다.
 * 로그인 요청 검증 → MemberService 인증 → 세션 생성,
 * 로그아웃 요청 → 세션 무효화의 흐름을 담당합니다.
 */
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobon.member.dto.LoginRequest;
import com.jobon.member.service.MemberService;
import com.jobon.member.vo.MemberVO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {
    private final MemberService memberService;

    public AuthApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 일반 로그인 API: 입력값 검증 → DB 회원 확인 → BCrypt 비밀번호 검사 → 로그인 세션 생성
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest,
            BindingResult bindingResult,
            HttpServletRequest request) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(error(bindingResult.getFieldErrors().get(0).getDefaultMessage()));
        try {
            MemberVO member = memberService.login(loginRequest.getLoginId(), loginRequest.getPassword());
            // 로그인 세션을 먼저 생성/획득한 뒤 세션 ID를 변경한다.
            // changeSessionId()는 기존 세션이 없는 상태에서 호출하면 예외가 발생할 수 있다.
            HttpSession session = request.getSession(true);
            request.changeSessionId();
            // JSP/Interceptor에서 로그인 여부와 회원 정보를 사용할 수 있도록 세션에 저장한다.
            session.setAttribute("loginMember", member);
            session.setAttribute("loginMemberId", member.getMemberId());
            session.setAttribute("loginId", member.getLoginId());
            String redirect = (String) session.getAttribute("redirectAfterLogin");
            session.removeAttribute("redirectAfterLogin");
            if (redirect == null || redirect.isBlank() || redirect.contains("/login"))
                redirect = request.getContextPath() + "/dashboard";
            return ResponseEntity.ok(Map.of("success", true, "redirect", redirect));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(401).body(error(e.getMessage()));
        }
    }

    // 로그아웃 API: 현재 HttpSession 전체를 만료시킨다.
    @PostMapping("/logout")
    public Map<String, Object> logout(HttpSession session) {
        session.invalidate();
        return Map.of("success", true, "redirect", "/main");
    }

    // API 오류 응답 형식을 {success:false, message:"..."} 형태로 통일한다.
    private Map<String, Object> error(String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", false);
        map.put("message", message);
        return map;
    }
}
