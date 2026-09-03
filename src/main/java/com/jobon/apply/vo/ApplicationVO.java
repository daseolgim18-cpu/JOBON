package com.jobon.apply.vo;

/** [추가] ApplicationVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import lombok.Data;

@Data
public class ApplicationVO {

    private Long applicationId;
    private Long memberId;
    private Long jobId;
    private Long companyId;
    private String companyName;
    private String jobTitle;
    private String status;
    private LocalDate appliedDate;
    private LocalDateTime nextScheduleAt;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** [추가] 대시보드 등 화면에서 ISO 형식(T 포함) 대신 읽기 쉬운 일정 시각을 표시합니다. */
    public String getNextScheduleAtLabel() {
        if (nextScheduleAt == null) {
            return "";
        }

        return nextScheduleAt.format(
                DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    /** [추가] datetime-local 입력값에는 초/밀리초가 붙지 않도록 분 단위 형식으로 제공합니다. */
    public String getNextScheduleAtInputValue() {
        if (nextScheduleAt == null) {
            return "";
        }

        return nextScheduleAt.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }

    /** [추가] 화면에서 지원일도 다른 날짜 표기와 동일하게 표시합니다. */
    public String getAppliedDateLabel() {
        if (appliedDate == null) {
            return "";
        }

        return appliedDate.format(
                DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    /** [추가] 화면에서 지원 상태 코드를 한글로 표시합니다. */
    public String getStatusLabel() {
        if (status == null) {
            return "";
        }

        return switch (status) {
            case "INTEREST" -> "관심";
            case "APPLIED" -> "지원완료";
            case "DOCUMENT" -> "서류";
            case "CODING_TEST" -> "코딩테스트";
            case "INTERVIEW" -> "면접";
            case "OFFER" -> "합격";
            case "REJECTED" -> "불합격";
            default -> status;
        };
    }

    /** [추가] 다음 일정까지 남은 날짜를 D-Day 형식으로 표시합니다. */
    public String getScheduleDdayLabel() {
        if (nextScheduleAt == null) {
            return "";
        }

        long days = ChronoUnit.DAYS.between(
                LocalDate.now(),
                nextScheduleAt.toLocalDate());

        if (days < 0) {
            return "지난 일정";
        }

        if (days == 0) {
            return "D-DAY";
        }

        return "D-" + days;
    }
}