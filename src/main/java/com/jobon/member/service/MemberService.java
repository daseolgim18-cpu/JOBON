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

    // [추가] 프로필 수정 시 현재 회원을 제외한 이메일 중복 확인
    boolean isEmailAvailable(String email, Long memberId);

    // [수정] 프로필 수정 시 현재 회원을 제외한 닉네임 사용 가능 여부를 확인한다.
    boolean isNicknameAvailable(String nickname, Long memberId);

    MemberVO join(JoinRequest request);

    MemberVO login(String loginId, String rawPassword);

    MemberVO findById(Long memberId);

    MemberVO findByEmail(String email);

    // [추가] 이름/이메일/휴대폰으로 로그인 아이디 조회
    String findLoginId(String name, String email, String phone);

    // [추가] 회원정보 확인 후 임시 비밀번호를 발급하고 가입 이메일로 발송
    void issueTemporaryPassword(String loginId, String name, String email);

    // [수정] 마이페이지 프로필 수정 후 갱신된 회원 정보를 반환한다.
    MemberVO updateProfile(MemberVO member);

    // [추가] 일반 로그인 비밀번호 보유 여부를 확인합니다.
    boolean hasLocalPassword(Long memberId);

    // [추가] 현재 비밀번호 검증 후 새 비밀번호를 BCrypt로 변경합니다.
    void changePassword(Long memberId, String currentPassword, String newPassword, String newPasswordConfirm);

    // [추가] 회원 데이터를 물리 삭제하지 않고 STATUS를 WITHDRAWN으로 변경합니다.
    void withdraw(Long memberId, String currentPassword);

    MemberVO createSocialMember(String provider, String providerUserId, String email, String name, String nickname,
            String profileImageUrl);

    void touchLastLogin(Long memberId);
}