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
import java.util.List;
import com.jobon.social.vo.SocialAccountVO;

@Mapper
public interface SocialAccountDAO {
    SocialAccountVO selectByProvider(@Param("provider") String provider,
            @Param("providerUserId") String providerUserId);

    int insertSocialAccount(SocialAccountVO socialAccount);

    int updateLastLoginAt(@Param("socialAccountId") Long socialAccountId);

    // [추가] 회원의 전체 SNS 연동 목록을 조회합니다.
    List<SocialAccountVO> selectByMemberId(@Param("memberId") Long memberId);

    // [추가] 한 회원이 같은 SNS 제공자를 중복 연동하지 않도록 회원+제공자로 조회합니다.
    SocialAccountVO selectByMemberAndProvider(@Param("memberId") Long memberId, @Param("provider") String provider);

    // [추가] 회원의 특정 SNS 연동 정보만 삭제합니다. JOBON_MEMBER 자체는 삭제하지 않습니다.
    int deleteByMemberAndProvider(@Param("memberId") Long memberId, @Param("provider") String provider);

    // [추가] SNS 전용 회원의 마지막 로그인 수단 해제 방지를 위해 연동 개수를 조회합니다.
    int countByMemberId(@Param("memberId") Long memberId);
}