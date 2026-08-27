package com.jobon.social.dao;


/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * SOCIAL_ACCOUNT 테이블 접근 DAO입니다.
 * SNS 제공자 + 제공자 회원ID로 연동 계정을 조회/등록하고 마지막 로그인 시각을 갱신합니다.
 */
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.jobon.social.vo.SocialAccountVO;

@Mapper
public interface SocialAccountDAO {
    SocialAccountVO selectByProvider(@Param("provider") String provider, @Param("providerUserId") String providerUserId);
    int insertSocialAccount(SocialAccountVO socialAccount);
    int updateLastLoginAt(@Param("socialAccountId") Long socialAccountId);
}
