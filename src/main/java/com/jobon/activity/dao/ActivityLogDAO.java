package com.jobon.activity.dao;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] ACTIVITY_LOG 등록/조회 DAO입니다.
 */
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.jobon.activity.vo.ActivityLogVO;

@Mapper
public interface ActivityLogDAO {
    int insert(ActivityLogVO vo);

    List<ActivityLogVO> selectList(@Param("memberId") Long memberId,
            @Param("activityType") String activityType);

    List<ActivityLogVO> selectRecent(@Param("memberId") Long memberId,
            @Param("limit") int limit);
}
