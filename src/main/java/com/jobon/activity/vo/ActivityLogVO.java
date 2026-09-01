package com.jobon.activity.vo;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] ACTIVITY_LOG 테이블과 활동 내역 화면 사이에서 사용하는 VO입니다.
 * 활동 유형/동작 유형/대상 ID/제목/발생 시각을 전달합니다.
 */
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class ActivityLogVO {
    private Long activityId;
    private Long memberId;
    private String activityType;
    private String actionType;
    private Long targetId;
    private String title;
    private LocalDateTime createdAt;

    /** 화면에서 영문 코드 대신 한글 활동 유형을 표시한다. */
    public String getActivityTypeLabel() {
        if (activityType == null) return "기타";
        return switch (activityType) {
            case "COMPANY" -> "기업";
            case "JOB" -> "채용공고";
            case "APPLICATION" -> "지원 현황";
            case "TODO" -> "TODO";
            case "LEARNING" -> "성장 기록";
            case "PROJECT" -> "프로젝트";
            case "AI" -> "AI 분석";
            default -> activityType;
        };
    }

    /** 활동 동작 코드를 화면용 한글로 변환한다. */
    public String getActionTypeLabel() {
        if (actionType == null) return "처리";
        return switch (actionType) {
            case "CREATE" -> "등록";
            case "UPDATE" -> "수정";
            case "DELETE" -> "삭제";
            case "ANALYZE" -> "분석";
            default -> actionType;
        };
    }

    /** 목록에서 사용하기 좋은 상대 시간을 계산한다. */
    public String getRelativeTime() {
        if (createdAt == null) return "";

        LocalDateTime now = LocalDateTime.now();
        if (createdAt.isAfter(now)) return getFormattedCreatedAt();

        Duration duration = Duration.between(createdAt, now);
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (minutes < 1) return "방금 전";
        if (minutes < 60) return minutes + "분 전";
        if (hours < 24) return hours + "시간 전";
        if (days == 1) return "어제";
        if (days < 7) return days + "일 전";
        return getFormattedCreatedAt();
    }

    /** 상대 시간이 길어진 경우 정확한 발생 시각을 표시한다. */
    public String getFormattedCreatedAt() {
        return createdAt == null ? "" : createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }
}
