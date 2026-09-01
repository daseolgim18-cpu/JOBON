package com.jobon.activity.service;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] 사용자 활동 기록 등록/조회 서비스 인터페이스입니다.
 */
import java.util.List;
import com.jobon.activity.vo.ActivityLogVO;

public interface ActivityLogService {
    List<ActivityLogVO> list(Long memberId, String activityType);

    List<ActivityLogVO> recent(Long memberId, int limit);

    void record(Long memberId, String activityType, String actionType, Long targetId, String title);
}
