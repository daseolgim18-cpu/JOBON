package com.jobon.ai.vo;

/** [추가] AI_EXPERIENCE_RECOMMEND VO */
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class AiExperienceRecommendVO {
    private Long recommendId;
    private Long analysisId;
    private Long projectId;
    private String projectName;
    private Integer rankNo;
    private String reason;
    private String sourceDetail;
    private String savedYn;
    private LocalDateTime createdAt;
}