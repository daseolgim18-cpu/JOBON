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

    // [수정] 프로필 수정 시 현재 회원을 제외한 닉네임 사용 가능 여부를 확인한다.
    boolean isNicknameAvailable(String nickname, Long memberId);

    MemberVO join(JoinRequest request);

    MemberVO login(String loginId, String rawPassword);

    MemberVO findById(Long memberId);

    MemberVO findByEmail(String email);

    // [수정] 마이페이지 프로필 수정 후 갱신된 회원 정보를 반환한다.
    MemberVO updateProfile(MemberVO member);

    MemberVO createSocialMember(String provider, String providerUserId, String email, String name, String nickname,
            String profileImageUrl);

    void touchLastLogin(Long memberId);
}