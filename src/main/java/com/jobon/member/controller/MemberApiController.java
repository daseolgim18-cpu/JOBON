package com.jobon.member.controller;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 회원가입 REST API Controller입니다.
 * 아이디/이메일 중복 확인과 회원가입 요청의 서버측 Validation 처리를 담당합니다.
 */
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jobon.member.dto.JoinRequest;
import com.jobon.member.service.MemberService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;
    public MemberApiController(MemberService memberService) { this.memberService = memberService; }

    // 회원가입 전에 로그인 아이디 중복 여부를 실시간 확인한다.
    @GetMapping("/check-login-id")
    public Map<String, Object> checkLoginId(@RequestParam String loginId) {
        return Map.of("available", memberService.isLoginIdAvailable(loginId));
    }

    // 회원가입 전에 이메일 중복 여부를 실시간 확인한다.
    @GetMapping("/check-email")
    public Map<String, Object> checkEmail(@RequestParam String email) {
        return Map.of("available", memberService.isEmailAvailable(email));
    }

    // 최종 회원가입 API: DTO Validation과 비밀번호 일치 여부를 검사한 뒤 DB에 저장한다.
    @PostMapping
    public ResponseEntity<Map<String, Object>> join(@Valid @RequestBody JoinRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().body(error(bindingResult.getFieldErrors().get(0).getDefaultMessage()));
        if (!request.isPasswordMatched()) return ResponseEntity.badRequest().body(error("비밀번호와 비밀번호 확인이 일치하지 않습니다."));
        try {
            memberService.join(request);
            return ResponseEntity.ok(Map.of("success", true, "redirect", "/member/join/complete"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(error(e.getMessage()));
        }
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", false);
        map.put("message", message);
        return map;
    }
}
