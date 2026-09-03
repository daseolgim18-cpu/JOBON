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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

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

    @Value("${LLM_MODEL:}")
    private String model;

    /**
     * [추가] 외부 LLM이 technologies 배열을 비워 반환한 경우에도
     * 공고 원문/분석 문장에 실제로 명시된 기술명만 보조 추출하기 위한 목록입니다.
     * 원문에 없는 기술을 임의로 만들어내지 않습니다.
     */
    private static final List<String> KNOWN_TECHNOLOGIES = List.of(
            "Java", "Spring Boot", "Spring MVC", "Spring", "JSP", "JSTL",
            "MyBatis", "JPA", "Hibernate", "Oracle", "Oracle Database", "MySQL",
            "PostgreSQL", "SQL", "JavaScript", "TypeScript", "React", "Vue",
            "HTML", "HTML5", "CSS", "CSS3", "REST API", "RESTful API",
            "Git", "GitHub", "GitHub Actions", "Jenkins", "CI/CD", "Docker",
            "Kubernetes", "K8s", "AWS", "Amazon Web Services", "GCP",
            "Google Cloud Platform", "Azure", "Linux", "Redis", "Kafka", "Python"
    );

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
        if (url == null || url.isBlank() || key == null || key.isBlank()
                || model == null || model.isBlank()) {
            throw new IllegalStateException("LLM API URL/Key/Model 미설정");
        }

        String prompt = "다음 채용공고 원문만 근거로 분석하고 반드시 JSON 객체 하나만 반환하세요. "
                + "반환 키는 정확히 summary, mainTasks, qualifications, preferences, requiredCompetencies, technologies 여섯 개를 사용하세요. "
                + "summary는 5문장 이내, mainTasks/qualifications/preferences/requiredCompetencies는 항목별 줄바꿈 문자열로 작성하세요. "
                + "technologies는 반드시 JSON 배열로 반환하고 각 원소는 {\"name\":\"기술명\",\"type\":\"REQUIRED 또는 PREFERRED\"} 형식으로 작성하세요. "
                + "Java, Spring Boot, Oracle, MyBatis, JavaScript, Docker, AWS처럼 원문에 실제로 명시된 개발 기술·프레임워크·DB·클라우드·도구만 technologies에 포함하세요. "
                + "자격요건에 명시된 기술은 REQUIRED, 우대사항에 명시된 기술은 PREFERRED로 분류하세요. "
                + "같은 기술은 한 번만 넣고, 기술명이 원문에 전혀 없으면 technologies는 빈 배열 []로 반환하세요. "
                + "학력·경력연차·자격증·문서작성능력·커뮤니케이션 같은 일반 조건을 technologies에 넣지 마세요. "
                + "원문에 없는 기술이나 경력 조건은 절대 추측하지 마세요.\n"
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

            JsonNode analysisJson = mapper.readTree(stripCodeFence(content));

            LlmAnalysisResponse response = new LlmAnalysisResponse();
            response.setSummary(text(analysisJson, "summary"));
            response.setMainTasks(text(analysisJson, "mainTasks"));
            response.setQualifications(text(analysisJson, "qualifications"));
            response.setPreferences(text(analysisJson, "preferences"));
            response.setRequiredCompetencies(text(analysisJson, "requiredCompetencies"));

            List<LlmAnalysisResponse.Tech> technologies = parseTechnologies(analysisJson);

            // [추가] 일부 OpenAI 호환 모델은 본문 필드는 정상 생성하면서 technologies 배열만
            // 비워 반환하는 경우가 있어, 공고 원문/분석 결과에 실제로 존재하는 기술명만 보조 추출합니다.
            if (technologies.isEmpty()) {
                technologies = extractTechnologiesFromText(request, response);
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


    /** [추가] technologies 배열을 다양한 OpenAI 호환 응답 형태에서도 안전하게 읽습니다. */
    private List<LlmAnalysisResponse.Tech> parseTechnologies(JsonNode analysisJson) {
        Map<String, LlmAnalysisResponse.Tech> unique = new LinkedHashMap<>();
        JsonNode technologiesNode = analysisJson.path("technologies");

        if (technologiesNode.isArray()) {
            for (JsonNode technologyNode : technologiesNode) {
                String techName;
                String requirementType;

                if (technologyNode.isTextual()) {
                    techName = technologyNode.asText("").trim();
                    requirementType = "REQUIRED";
                } else {
                    techName = firstNonBlank(
                            text(technologyNode, "name"),
                            text(technologyNode, "techName"),
                            text(technologyNode, "technology")
                    ).trim();
                    requirementType = firstNonBlank(
                            text(technologyNode, "type"),
                            text(technologyNode, "requirementType"),
                            "REQUIRED"
                    ).trim().toUpperCase(Locale.ROOT);
                }

                addTechnology(unique, techName, requirementType);
            }
        }

        return new ArrayList<>(unique.values());
    }

    /**
     * [추가] LLM이 technologies 배열을 누락한 경우를 위한 보조 추출입니다.
     * KNOWN_TECHNOLOGIES 중 실제 공고 원문 또는 분석 결과에 등장한 이름만 저장합니다.
     */
    private List<LlmAnalysisResponse.Tech> extractTechnologiesFromText(
            LlmAnalysisRequest request, LlmAnalysisResponse response) {

        String requiredText = String.join("\n",
                nullToEmpty(request.getOriginalText()),
                nullToEmpty(response.getMainTasks()),
                nullToEmpty(response.getQualifications()),
                nullToEmpty(response.getRequiredCompetencies()));

        String preferredText = nullToEmpty(response.getPreferences());
        Map<String, LlmAnalysisResponse.Tech> unique = new LinkedHashMap<>();

        for (String technology : KNOWN_TECHNOLOGIES) {
            boolean inPreferred = containsTechnology(preferredText, technology);
            boolean inRequired = containsTechnology(requiredText, technology);

            if (!inPreferred && !inRequired) {
                continue;
            }

            addTechnology(unique, technology, inRequired ? "REQUIRED" : "PREFERRED");
        }

        return new ArrayList<>(unique.values());
    }

    private void addTechnology(Map<String, LlmAnalysisResponse.Tech> unique,
            String techName, String requirementType) {
        if (techName == null || techName.isBlank()) {
            return;
        }

        String type = "PREFERRED".equalsIgnoreCase(requirementType) ? "PREFERRED" : "REQUIRED";
        String key = normalizeTechKey(techName);
        if (key.isBlank()) {
            return;
        }

        LlmAnalysisResponse.Tech old = unique.get(key);
        if (old == null || "REQUIRED".equals(type)) {
            unique.put(key, new LlmAnalysisResponse.Tech(techName.trim(), type));
        }
    }

    private boolean containsTechnology(String source, String technology) {
        if (source == null || source.isBlank() || technology == null || technology.isBlank()) {
            return false;
        }

        String normalizedSource = source.toLowerCase(Locale.ROOT);
        String normalizedTechnology = technology.toLowerCase(Locale.ROOT);

        // C++, C#, CI/CD처럼 경계식이 불안정한 기술명도 있어 quote 기반으로 검색합니다.
        return Pattern.compile(Pattern.quote(normalizedTechnology), Pattern.CASE_INSENSITIVE)
                .matcher(normalizedSource)
                .find();
    }

    private String normalizeTechKey(String value) {
        if (value == null) {
            return "";
        }
        return value.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9가-힣+#]", "");
    }

    /** [추가] 모델이 ```json 코드펜스를 붙여도 JSON 파싱이 가능하도록 제거합니다. */
    private String stripCodeFence(String value) {
        if (value == null) {
            return "";
        }
        String trimmed = value.trim();
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.replaceFirst("^```(?:json)?\\s*", "");
            trimmed = trimmed.replaceFirst("\\s*```$", "");
        }
        return trimmed.trim();
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return "";
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
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
