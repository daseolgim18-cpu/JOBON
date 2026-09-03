package com.jobon.member.dao;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * JOBON_MEMBER 테이블 접근 DAO입니다.
 * MyBatis SqlSession을 통해 memberMapper.xml의 SQL을 호출합니다.
 */
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.jobon.member.vo.MemberVO;

@Mapper
public interface MemberDAO {
    int countByLoginId(@Param("loginId") String loginId);

    int countByEmail(@Param("email") String email);

    // [추가] 프로필 이메일 변경 시 현재 회원을 제외한 이메일 중복 여부를 확인합니다.
    int countByEmailExcludingMember(@Param("email") String email, @Param("memberId") Long memberId);

    // [수정] 프로필 수정 시 현재 회원을 제외하고 동일한 닉네임이 존재하는지 확인한다.
    int countByNicknameExcludingMember(@Param("nickname") String nickname, @Param("memberId") Long memberId);

    MemberVO selectByLoginId(@Param("loginId") String loginId);

    MemberVO selectByEmail(@Param("email") String email);

    MemberVO selectByMemberId(@Param("memberId") Long memberId);

    // [추가] 아이디 찾기용 회원 조회
    MemberVO selectForFindId(@Param("name") String name, @Param("email") String email, @Param("phone") String phone);

    // [추가] 비밀번호 찾기용 회원 조회
    MemberVO selectForPasswordReset(@Param("loginId") String loginId, @Param("name") String name, @Param("email") String email);

    int insertMember(MemberVO member);

    // [수정] 마이페이지에서 수정 가능한 프로필 항목을 갱신한다.
    int updateProfile(MemberVO member);

    int updateLastLoginAt(@Param("memberId") Long memberId);

    // [추가] 비밀번호 변경
    int updatePassword(@Param("memberId") Long memberId, @Param("passwordHash") String passwordHash);

    // [추가] 회원 탈퇴는 연관 데이터 보존을 위해 상태값만 변경합니다.
    int updateStatusWithdrawn(@Param("memberId") Long memberId);
}