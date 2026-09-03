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
    // [추가] 저장한 경험 목록에서 원본 채용공고를 함께 표시하기 위한 조회 전용 필드입니다.
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private Integer rankNo;
    private String reason;
    private String sourceDetail;
    private String savedYn;
    private LocalDateTime createdAt;
}