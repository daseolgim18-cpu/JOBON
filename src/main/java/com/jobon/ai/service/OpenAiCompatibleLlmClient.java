package com.jobon.ai.service;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [수정] Java 21 + Spring Boot 4.0.8 환경에 맞춘
 * OpenAI 호환 Chat Completions 형식의 LLM 호출 구현체입니다.
 *
 * Spring Boot 4는 Jackson 3을 기본 JSON 라이브러리로 사용하므로
 * tools.jackson.databind 패키지를 사용합니다.
 *
 * LLM API URL/Key가 없거나 외부 호출에 실패하면 예외를 전달하고,
 * 상위 AiAnalysisServiceImpl에서 LocalJobAnalyzer로 폴백합니다.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.jobon.ai.dto.LlmAnalysisRequest;
import com.jobon.ai.dto.LlmAnalysisResponse;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class OpenAiCompatibleLlmClient implements LlmClient {

    private final RestClient client;
    private final ObjectMapper mapper;

    @Value("${LLM_API_URL:}")
    private String url;

    @Value("${LLM_API_KEY:}")
    private String key;

    @Value("${LLM_MODEL:gpt-4.1-mini}")
    private String model;

    /**
     * [수정]
     * Spring Boot가 관리하는 Jackson 3 ObjectMapper를 주입받아 사용합니다.
     * 직접 new ObjectMapper() 하지 않으므로 Boot의 JSON 설정과 일관성을 유지합니다.
     */
    public OpenAiCompatibleLlmClient(ObjectMapper mapper) {
        this.client = RestClient.create();
        this.mapper = mapper;
    }

    @Override
    public LlmAnalysisResponse analyze(LlmAnalysisRequest request) {
        if (url == null || url.isBlank() || key == null || key.isBlank()) {
            throw new IllegalStateException("LLM API URL/Key 미설정");
        }

        String prompt = "다음 채용공고 원문만 근거로 분석하고 JSON 객체만 반환하세요. "
                + "키는 summary, mainTasks, qualifications, preferences, requiredCompetencies, technologies입니다. "
                + "summary는 5문장 이내, 업무·자격·우대사항은 항목별 줄바꿈 문자열로 정리하세요. "
                + "technologies는 중복 없이 [{name,type}] 형식으로 작성하고 type은 REQUIRED 또는 PREFERRED만 사용하세요. "
                + "필수와 우대가 불명확하면 문맥상 자격요건에 있는 기술은 REQUIRED, 우대사항에 있는 기술은 PREFERRED로 분류하세요. "
                + "원문에 없는 기술이나 경력 조건은 추측하지 마세요.\n"
                + "공고명: " + nullToEmpty(request.getTitle()) + "\n"
                + "직무: " + nullToEmpty(request.getJobRole()) + "\n"
                + "원문: " + nullToEmpty(request.getOriginalText());

        Map<String, Object> body = Map.of(
                "model", model,
                "temperature", 0.2,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of(
                                "role", "system",
                                "content", "당신은 한국 채용공고 분석기입니다. "
                                        + "사실을 과장하지 말고 채용공고 원문에서만 추출하세요."
                        ),
                        Map.of(
                                "role", "user",
                                "content", prompt
                        )
                )
        );

        final String rawResponse;
        try {
            rawResponse = client.post()
                    .uri(url)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + key)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            throw new IllegalStateException("LLM API 호출 실패", e);
        }

        if (rawResponse == null || rawResponse.isBlank()) {
            throw new IllegalStateException("LLM API 응답이 비어 있습니다.");
        }

        try {
            JsonNode root = mapper.readTree(rawResponse);
            String content = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");

            if (content.isBlank()) {
                throw new IllegalStateException("LLM 응답 content가 비어 있습니다.");
            }

            JsonNode analysisJson = mapper.readTree(content);

            LlmAnalysisResponse response = new LlmAnalysisResponse();
            response.setSummary(text(analysisJson, "summary"));
            response.setMainTasks(text(analysisJson, "mainTasks"));
            response.setQualifications(text(analysisJson, "qualifications"));
            response.setPreferences(text(analysisJson, "preferences"));
            response.setRequiredCompetencies(text(analysisJson, "requiredCompetencies"));

            List<LlmAnalysisResponse.Tech> technologies = new ArrayList<>();
            JsonNode technologiesNode = analysisJson.path("technologies");

            if (technologiesNode.isArray()) {
                for (JsonNode technologyNode : technologiesNode) {
                    String techName = text(technologyNode, "name").trim();
                    String requirementType = text(technologyNode, "type").trim().toUpperCase();

                    if (techName.isBlank()) {
                        continue;
                    }

                    if (!"REQUIRED".equals(requirementType)
                            && !"PREFERRED".equals(requirementType)) {
                        requirementType = "REQUIRED";
                    }

                    technologies.add(
                            new LlmAnalysisResponse.Tech(techName, requirementType)
                    );
                }
            }

            response.setTechnologies(technologies);
            response.setRawResponse(rawResponse);
            return response;

        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("LLM 응답 파싱 실패", e);
        }
    }

    private String text(JsonNode node, String key) {
        if (node == null || key == null) {
            return "";
        }
        return node.path(key).asText("");
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
