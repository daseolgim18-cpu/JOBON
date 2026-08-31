package com.jobon.ai.vo;

/** [추가] AI_JOB_TECH VO */
import lombok.Data;

@Data
public class AiJobTechVO {
    private Long aiJobTechId;
    private Long analysisId;
    private String techName;
    private String requirementType;
    private String matchStatus;
}