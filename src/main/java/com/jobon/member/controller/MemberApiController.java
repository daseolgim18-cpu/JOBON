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
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {
    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

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

    /**
     * [수정] 프로필 수정 시 현재 로그인 회원을 제외하고 닉네임 중복 여부를 확인한다.
     * 현재 사용 중인 자신의 닉네임은 사용 가능한 것으로 처리한다.
     */
    @GetMapping("/check-nickname")
    public Map<String, Object> checkNickname(@RequestParam String nickname,
            HttpSession session) {

        Object memberIdValue = session.getAttribute("loginMemberId");

        if (!(memberIdValue instanceof Number number)) {
            return Map.of("available", false, "message", "로그인 정보를 확인할 수 없습니다.");
        }

        boolean available = memberService.isNicknameAvailable(nickname, number.longValue());

        return Map.of("available", available, "message", available ? "사용 가능한 닉네임입니다." : "이미 사용 중인 닉네임입니다.");
    }

    // 최종 회원가입 API: DTO Validation과 비밀번호 일치 여부를 검사한 뒤 DB에 저장한다.
    @PostMapping
    public ResponseEntity<Map<String, Object>> join(@Valid @RequestBody JoinRequest request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return ResponseEntity.badRequest().body(error(bindingResult.getFieldErrors().get(0).getDefaultMessage()));
        if (!request.isPasswordMatched())
            return ResponseEntity.badRequest().body(error("비밀번호와 비밀번호 확인이 일치하지 않습니다."));
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
