package com.jobon.member.dto;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 일반 로그인 요청 DTO입니다.
 * 사용자가 입력한 아이디와 비밀번호를 전달받고 필수값을 검증합니다.
 */
import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String loginId;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
