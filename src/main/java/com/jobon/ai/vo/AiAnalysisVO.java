package com.jobon.ai.vo;

/** [추가] AI_ANALYSIS VO */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.Data;

@Data
public class AiAnalysisVO {
    private Long analysisId;
    private Long memberId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String status;
    private String summary;
    private String mainTasks;
    private String qualifications;
    private String preferences;
    private String requiredCompetencies;
    private String rawResponse;
    private String errorMessage;
    private String modelName;
    private LocalDateTime requestedAt;
    private LocalDateTime completedAt;
    private List<AiJobTechVO> techs = new ArrayList<>();
    private List<AiExperienceRecommendVO> recommendations = new ArrayList<>();
    private Integer readinessScore;
    private List<String> interviewQuestions = new ArrayList<>();

    /** [추가] 화면에서 밀리초가 노출되지 않도록 분석 요청 시각을 통일된 형식으로 표시합니다. */
    public String getRequestedAtLabel() {
        if (requestedAt == null) return "";
        return requestedAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    /** [추가] DB 상태 코드는 유지하고 사용자 화면에서만 읽기 쉬운 한글 상태로 표시합니다. */
    public String getStatusLabel() {
        if (status == null) return "";
        return switch (status) {
            case "PROCESSING" -> "분석중";
            case "COMPLETED" -> "분석완료";
            case "FAILED" -> "분석실패";
            default -> status;
        };
    }
}
