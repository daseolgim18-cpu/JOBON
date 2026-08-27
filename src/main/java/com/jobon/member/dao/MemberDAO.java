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

    // [수정] 프로필 수정 시 현재 회원을 제외하고 동일한 닉네임이 존재하는지 확인한다.
    int countByNicknameExcludingMember(@Param("nickname") String nickname, @Param("memberId") Long memberId);

    MemberVO selectByLoginId(@Param("loginId") String loginId);

    MemberVO selectByEmail(@Param("email") String email);

    MemberVO selectByMemberId(@Param("memberId") Long memberId);

    int insertMember(MemberVO member);

    // [수정] 마이페이지에서 수정 가능한 프로필 항목을 갱신한다.
    int updateProfile(MemberVO member);

    int updateLastLoginAt(@Param("memberId") Long memberId);
}