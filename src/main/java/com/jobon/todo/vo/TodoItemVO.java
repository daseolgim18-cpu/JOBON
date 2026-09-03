package com.jobon.todo.vo;

/** [추가] TodoItemVO DB/화면 전달용 VO */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import lombok.Data;

@Data
public class TodoItemVO {

    private Long todoId;
    private Long memberId;
    private Long companyId;
    private Long jobId;
    private String companyName;
    private String jobTitle;
    private String title;
    private String priority;
    private LocalDate dueDate;
    private String status;
    private String memo;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** [추가] 화면에서 영문 우선순위 코드 대신 한글을 표시합니다. */
    public String getPriorityLabel() {
        if (priority == null) {
            return "";
        }

        return switch (priority) {
            case "HIGH" -> "높음";
            case "MEDIUM" -> "보통";
            case "LOW" -> "낮음";
            default -> priority;
        };
    }

    /** [추가] 화면에서 날짜를 JOBON 공통 표기 형식으로 표시합니다. */
    public String getDueDateLabel() {
        return dueDate == null
                ? "마감일 없음"
                : dueDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    /** [추가] TODO 마감일을 D-Day 형식으로 표시합니다. */
    public String getDueDdayLabel() {
        if (dueDate == null) {
            return "";
        }

        long days = ChronoUnit.DAYS.between(LocalDate.now(), dueDate);

        if (days < 0) {
            return "기한 지남";
        }

        if (days == 0) {
            return "D-DAY";
        }

        return "D-" + days;
    }
}