package com.jobon.member.service;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 회원 도메인의 Service 인터페이스입니다.
 * Controller가 구현체에 직접 의존하지 않도록 회원가입/로그인/조회 기능의 규약을 정의합니다.
 */
import com.jobon.member.dto.JoinRequest;
import com.jobon.member.vo.MemberVO;

public interface MemberService {
    boolean isLoginIdAvailable(String loginId);
    boolean isEmailAvailable(String email);
    MemberVO join(JoinRequest request);
    MemberVO login(String loginId, String rawPassword);
    MemberVO findById(Long memberId);
    MemberVO findByEmail(String email);
    MemberVO createSocialMember(String provider, String providerUserId, String email, String name, String nickname, String profileImageUrl);
    void touchLastLogin(Long memberId);
}
