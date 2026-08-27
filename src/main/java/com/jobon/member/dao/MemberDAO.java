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
    MemberVO selectByLoginId(@Param("loginId") String loginId);
    MemberVO selectByEmail(@Param("email") String email);
    MemberVO selectByMemberId(@Param("memberId") Long memberId);
    int insertMember(MemberVO member);
    int updateLastLoginAt(@Param("memberId") Long memberId);
}
