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
    // [추가] 부족·부분일치 기술별 맞춤 학습 방향을 화면에 전달합니다.
    private String learningDirection;
}
