package com.jobon.activity.service;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] ACTIVITY_LOG 등록/조회 비즈니스 로직입니다.
 * 각 도메인 CRUD 서비스의 같은 트랜잭션 안에서 호출되어 실제 작업이 성공한 경우에만
 * 활동 내역이 남도록 구성합니다.
 */
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import com.jobon.activity.dao.ActivityLogDAO;
import com.jobon.activity.vo.ActivityLogVO;

@Service
public class ActivityLogServiceImpl implements ActivityLogService {
    private static final Set<String> ALLOWED_TYPES = Set.of(
            "COMPANY", "JOB", "APPLICATION", "TODO", "LEARNING", "PROJECT", "AI");
    private static final Set<String> ALLOWED_ACTIONS = Set.of("CREATE", "UPDATE", "DELETE", "ANALYZE");

    private final ActivityLogDAO dao;

    public ActivityLogServiceImpl(ActivityLogDAO dao) {
        this.dao = dao;
    }

    @Override
    public List<ActivityLogVO> list(Long memberId, String activityType) {
        requireMemberId(memberId);
        String normalizedType = normalizeType(activityType);
        return dao.selectList(memberId, normalizedType);
    }

    @Override
    public List<ActivityLogVO> recent(Long memberId, int limit) {
        requireMemberId(memberId);
        int safeLimit = Math.max(1, Math.min(limit, 20));
        return dao.selectRecent(memberId, safeLimit);
    }

    @Override
    public void record(Long memberId, String activityType, String actionType, Long targetId, String title) {
        requireMemberId(memberId);

        String type = normalizeRequired(activityType, ALLOWED_TYPES, "활동 유형이 올바르지 않습니다.");
        String action = normalizeRequired(actionType, ALLOWED_ACTIONS, "활동 동작 유형이 올바르지 않습니다.");
        String safeTitle = title == null ? "" : title.trim();
        if (safeTitle.isEmpty()) safeTitle = "JOBON 활동";
        if (safeTitle.length() > 300) safeTitle = safeTitle.substring(0, 300);

        ActivityLogVO vo = new ActivityLogVO();
        vo.setMemberId(memberId);
        vo.setActivityType(type);
        vo.setActionType(action);
        vo.setTargetId(targetId);
        vo.setTitle(safeTitle);

        if (dao.insert(vo) != 1) {
            throw new IllegalStateException("활동 내역 저장에 실패했습니다.");
        }
    }

    private void requireMemberId(Long memberId) {
        if (memberId == null) throw new IllegalArgumentException("로그인 정보가 없습니다.");
    }

    private String normalizeType(String activityType) {
        if (activityType == null || activityType.isBlank()) return null;
        String type = activityType.trim().toUpperCase();
        return ALLOWED_TYPES.contains(type) ? type : null;
    }

    private String normalizeRequired(String value, Set<String> allowed, String message) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(message);
        String normalized = value.trim().toUpperCase();
        if (!allowed.contains(normalized)) throw new IllegalArgumentException(message);
        return normalized;
    }
}
