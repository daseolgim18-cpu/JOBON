package com.jobon.ai.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.jobon.ai.dto.LlmAnalysisRequest;

class LocalJobAnalyzerTest {
    private final LocalJobAnalyzer analyzer = new LocalJobAnalyzer();

    @Test
    void 기술이등장한문맥으로필수와우대를구분한다() {
        String originalText = "필수 기술: Java 기반 백엔드 개발 경험\n"
                + "일반 직무 설명 ".repeat(20)
                + "\n우대 기술: Docker 컨테이너 운영 경험";

        var response = analyzer.analyze(LlmAnalysisRequest.builder()
                .title("백엔드 개발자").jobRole("백엔드")
                .originalText(originalText).build());

        assertTrue(response.getTechnologies().stream()
                .anyMatch(x -> "Java".equals(x.getName()) && "REQUIRED".equals(x.getType())));
        assertTrue(response.getTechnologies().stream()
                .anyMatch(x -> "Docker".equals(x.getName()) && "PREFERRED".equals(x.getType())));
    }

    @Test
    void 원문에없는기술을추가하지않는다() {
        var response = analyzer.analyze(LlmAnalysisRequest.builder()
                .title("서버 개발자").jobRole("백엔드")
                .originalText("필수 기술: Java, Oracle, SQL").build());

        assertEquals(3, response.getTechnologies().size());
    }
}
