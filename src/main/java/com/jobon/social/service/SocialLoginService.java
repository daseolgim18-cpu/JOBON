package com.jobon.social.service;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * SNS 로그인 Service 인터페이스입니다.
 * SNS 인증 URL 생성과 Callback 인증 처리 기능을 정의합니다.
 */
import com.jobon.member.vo.MemberVO;

public interface SocialLoginService {
    String createLoginUrl(String provider, String state);
    MemberVO login(String provider, String code, String state);
}
