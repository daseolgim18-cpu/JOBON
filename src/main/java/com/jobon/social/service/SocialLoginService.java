package com.jobon.social.service;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * SNS 로그인 Service 인터페이스입니다.
 * SNS 인증 URL 생성과 Callback 인증 처리 기능을 정의합니다.
 */
import java.util.List;

import com.jobon.member.vo.MemberVO;
import com.jobon.social.vo.SocialAccountVO;

public interface SocialLoginService {
    String createLoginUrl(String provider, String state);

    MemberVO login(String provider, String code, String state);

    // [추가] 현재 JOBON 회원에게 SNS 계정을 연결합니다.
    void linkAccount(Long memberId, String provider, String code, String state);

    // [추가] 현재 회원의 SNS 연동을 해제합니다. SNS 전용 회원의 마지막 로그인 수단은 해제할 수 없습니다.
    void unlinkAccount(Long memberId, String provider);

    // [추가] 연동 계정 관리 화면에서 사용할 현재 회원의 SNS 연동 목록을 조회합니다.
    List<SocialAccountVO> findLinkedAccounts(Long memberId);
}