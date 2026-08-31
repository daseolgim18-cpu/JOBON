package com.jobon.ai.vo;

/** [추가] AI_ANALYSIS VO */
import java.time.LocalDateTime;
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
}