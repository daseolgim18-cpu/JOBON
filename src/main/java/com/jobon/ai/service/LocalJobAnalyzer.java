package com.jobon.ai.service;

/** [추가] API Key가 없어도 개발/테스트가 가능하도록 제공하는 규칙 기반 폴백 분석기 */
import java.util.*;
import java.util.regex.*;
import org.springframework.stereotype.Component;
import com.jobon.ai.dto.*;

@Component
public class LocalJobAnalyzer {
    private static final List<String> TECH = List.of("Java", "Spring Boot", "Spring", "Oracle", "MyBatis", "JPA", "SQL",
            "JavaScript", "TypeScript", "React", "Vue", "AWS", "GCP", "Docker", "Kubernetes", "Git", "GitHub", "Linux",
            "Redis", "Kafka", "Python", "JSP", "HTML", "CSS", "REST API", "CI/CD");

    public LlmAnalysisResponse analyze(LlmAnalysisRequest r) {
        String text = Optional.ofNullable(r.getOriginalText()).orElse("");
        List<LlmAnalysisResponse.Tech> ts = new ArrayList<>();
        for (String t : TECH) {
            if (Pattern.compile(Pattern.quote(t), Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                String around = text.toLowerCase();
                String type = (around.contains("우대") || around.contains("preferred")) ? "PREFERRED" : "REQUIRED";
                ts.add(new LlmAnalysisResponse.Tech(t, type));
            }
        }
        String[] lines = text.split("[\r\n]+");
        String summary = clip(text, 350);
        String main = pick(lines, "업무", "담당", "role", "responsibil");
        String qual = pick(lines, "자격", "필수", "require");
        String pref = pick(lines, "우대", "preferred", "plus");
        return LlmAnalysisResponse.builder().summary(summary).mainTasks(main).qualifications(qual).preferences(pref)
                .requiredCompetencies(String.join(", ", ts.stream().map(LlmAnalysisResponse.Tech::getName).toList()))
                .technologies(ts).rawResponse("LOCAL_FALLBACK").build();
    }

    private String pick(String[] ls, String... keys) {
        List<String> r = new ArrayList<>();
        for (String l : ls) {
            for (String k : keys)
                if (l.toLowerCase().contains(k.toLowerCase())) {
                    r.add(l.trim());
                    break;
                }
            if (r.size() >= 6)
                break;
        }
        return r.isEmpty() ? "원문에서 명확한 항목을 자동 식별하지 못했습니다." : String.join("\n", r);
    }

    private String clip(String s, int n) {
        s = s.replaceAll("\\s+", " ").trim();
        return s.length() > n ? s.substring(0, n) + "..." : s;
    }
}
