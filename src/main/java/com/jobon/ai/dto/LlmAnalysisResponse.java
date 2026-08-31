package com.jobon.ai.dto;

/** [추가] LLM 분석 응답 DTO. JSON 구조를 내부 표준 형태로 정규화합니다. */
import java.util.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LlmAnalysisResponse {
    private String summary;
    private String mainTasks;
    private String qualifications;
    private String preferences;
    private String requiredCompetencies;
    @Builder.Default
    private List<Tech> technologies = new ArrayList<>();
    private String rawResponse;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tech {
        private String name;
        private String type;
    }
}