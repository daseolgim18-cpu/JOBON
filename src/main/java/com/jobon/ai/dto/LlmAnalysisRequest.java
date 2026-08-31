package com.jobon.ai.dto;

/** [추가] LLM 채용공고 분석 요청 DTO */
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmAnalysisRequest {
    private String title;
    private String jobRole;
    private String originalText;
}