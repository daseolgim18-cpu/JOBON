package com.jobon.ai.service;

/**
 * [수정] LLM 분석 결과 저장 → 기술 유사어/부분일치 분류 → 프로젝트 경험 TOP3 추천
 * → 준비도·부족 기술 학습 방향·면접 질문 구성을 담당합니다.
 */
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.ai.dao.AiAnalysisDAO;
import com.jobon.ai.dto.*;
import com.jobon.ai.vo.*;
import com.jobon.job.service.JobPostingService;
import com.jobon.job.vo.JobPostingVO;
import com.jobon.project.service.ProjectExperienceService;
import com.jobon.project.vo.*;

@Service
public class AiAnalysisServiceImpl implements AiAnalysisService {
    private static final Map<String, String> TECH_ALIASES = Map.ofEntries(
            Map.entry("js", "javascript"), Map.entry("javascript", "javascript"),
            Map.entry("ts", "typescript"), Map.entry("typescript", "typescript"),
            Map.entry("springframework", "spring"), Map.entry("spring", "spring"),
            Map.entry("springboot", "springboot"), Map.entry("springmvc", "springmvc"),
            Map.entry("oracledatabase", "oracle"), Map.entry("oracledb", "oracle"), Map.entry("oracle", "oracle"),
            Map.entry("restfulapi", "restapi"), Map.entry("restapi", "restapi"),
            Map.entry("cicd", "cicd"), Map.entry("githubactions", "githubactions"),
            Map.entry("k8s", "kubernetes"), Map.entry("kubernetes", "kubernetes"),
            Map.entry("amazonwebservices", "aws"), Map.entry("aws", "aws"),
            Map.entry("googlecloudplatform", "gcp"), Map.entry("gcp", "gcp"));

    private static final Map<String, String> TECH_FAMILIES = Map.ofEntries(
            Map.entry("spring", "spring"), Map.entry("springboot", "spring"), Map.entry("springmvc", "spring"),
            Map.entry("java", "java"), Map.entry("jsp", "java-web"), Map.entry("jstl", "java-web"),
            Map.entry("javascript", "frontend"), Map.entry("typescript", "frontend"),
            Map.entry("react", "frontend"), Map.entry("vue", "frontend"),
            Map.entry("oracle", "database"), Map.entry("mysql", "database"), Map.entry("postgresql", "database"),
            Map.entry("sql", "database"), Map.entry("mybatis", "persistence"), Map.entry("jpa", "persistence"),
            Map.entry("hibernate", "persistence"), Map.entry("git", "scm"), Map.entry("github", "scm"),
            Map.entry("githubactions", "cicd"), Map.entry("jenkins", "cicd"), Map.entry("cicd", "cicd"),
            Map.entry("docker", "container"), Map.entry("kubernetes", "container"),
            Map.entry("aws", "cloud"), Map.entry("gcp", "cloud"), Map.entry("azure", "cloud"));

    private final AiAnalysisDAO dao;
    private final JobPostingService jobs;
    private final ProjectExperienceService projects;
    private final LlmClient llm;
    private final LocalJobAnalyzer local;
    private final ActivityLogService activityLogService;

    public AiAnalysisServiceImpl(AiAnalysisDAO dao, JobPostingService jobs,
            ProjectExperienceService projects, LlmClient llm,
            LocalJobAnalyzer local, ActivityLogService activityLogService) {
        this.dao = dao;
        this.jobs = jobs;
        this.projects = projects;
        this.llm = llm;
        this.local = local;
        this.activityLogService = activityLogService;
    }

    @Override
    public List<AiAnalysisVO> list(Long memberId) { return dao.selectList(memberId); }

    @Override
    @Transactional
    public AiAnalysisVO analyze(Long memberId, Long jobId) {
        JobPostingVO job = jobs.get(memberId, jobId);
        AiAnalysisVO analysis = new AiAnalysisVO();
        analysis.setMemberId(memberId);
        analysis.setJobId(jobId);
        analysis.setStatus("PROCESSING");
        if (dao.insertAnalysis(analysis) != 1) {
            throw new IllegalStateException("AI 분석 요청을 저장하지 못했습니다.");
        }
        try {
            LlmAnalysisResponse response;
            try {
                response = llm.analyze(requestOf(job));
                analysis.setModelName("external-llm");
            } catch (Exception externalError) {
                response = local.analyze(requestOf(job));
                analysis.setModelName("local-fallback");
                analysis.setErrorMessage("외부 LLM 미사용/실패로 로컬 분석을 사용했습니다: "
                        + safeMessage(externalError));
            }
            normalizeResponse(response);
            analysis.setSummary(response.getSummary());
            analysis.setMainTasks(response.getMainTasks());
            analysis.setQualifications(response.getQualifications());
            analysis.setPreferences(response.getPreferences());
            analysis.setRequiredCompetencies(response.getRequiredCompetencies());
            analysis.setRawResponse(response.getRawResponse());
            analysis.setStatus("COMPLETED");
            if (dao.completeAnalysis(analysis) != 1) {
                throw new IllegalStateException("AI 분석 결과 저장에 실패했습니다.");
            }
            syncTechAndRecommend(memberId, analysis, job, response);
            activityLogService.record(memberId, "AI", "ANALYZE", analysis.getAnalysisId(),
                    job.getTitle() + " AI 분석 완료");
        } catch (Exception error) {
            // [수정] 예외를 다시 던져 트랜잭션 전체가 취소되지 않게 하고 FAILED 결과를 DB에 보존합니다.
            analysis.setStatus("FAILED");
            analysis.setErrorMessage("분석 처리 실패: " + safeMessage(error));
            dao.failAnalysis(analysis);
        }
        return get(memberId, analysis.getAnalysisId());
    }

    @Override
    public AiAnalysisVO get(Long memberId, Long id) {
        AiAnalysisVO analysis = dao.selectOne(memberId, id);
        if (analysis == null) throw new IllegalArgumentException("AI 분석 결과를 찾을 수 없습니다.");
        decorate(analysis);
        return analysis;
    }

    @Override
    public AiAnalysisVO getByJob(Long memberId, Long jobId) {
        AiAnalysisVO analysis = dao.selectLatestByJob(memberId, jobId);
        if (analysis == null) return analyze(memberId, jobId);
        decorate(analysis);
        return analysis;
    }

    @Override
    @Transactional
    public void saveRecommendation(Long memberId, Long id, boolean saved) {
        if (memberId == null || id == null
                || dao.updateRecommendationSaved(memberId, id, saved ? "Y" : "N") != 1) {
            throw new IllegalArgumentException("저장할 추천 결과를 찾을 수 없습니다.");
        }
    }


    /**
     * [추가] AI 분석 결과 삭제.
     * FK 제약조건을 지키기 위해 추천 → 기술 → 분석 본문 순서로 삭제하며,
     * 분석 본문은 MEMBER_ID까지 조건에 포함해 다른 사용자의 결과를 삭제할 수 없게 합니다.
     */
    @Override
    @Transactional
    public void delete(Long memberId, Long analysisId) {
        if (memberId == null || analysisId == null) {
            throw new IllegalArgumentException("삭제할 AI 분석 결과를 찾을 수 없습니다.");
        }
        AiAnalysisVO analysis = dao.selectOne(memberId, analysisId);
        if (analysis == null) {
            throw new IllegalArgumentException("삭제할 AI 분석 결과를 찾을 수 없습니다.");
        }
        dao.deleteRecommendations(analysisId);
        dao.deleteTechs(analysisId);
        if (dao.deleteAnalysis(memberId, analysisId) != 1) {
            throw new IllegalStateException("AI 분석 결과를 삭제하지 못했습니다.");
        }
        activityLogService.record(memberId, "AI", "DELETE", analysisId,
                analysis.getJobTitle() + " AI 분석 결과 삭제");
    }

    private LlmAnalysisRequest requestOf(JobPostingVO job) {
        return LlmAnalysisRequest.builder().title(job.getTitle()).jobRole(job.getJobRole())
                .originalText(job.getOriginalText()).build();
    }

    private void normalizeResponse(LlmAnalysisResponse response) {
        if (response == null) throw new IllegalStateException("분석 응답이 없습니다.");
        if (response.getTechnologies() == null) response.setTechnologies(new ArrayList<>());
        if (blank(response.getSummary()) && blank(response.getMainTasks())
                && blank(response.getQualifications())) {
            throw new IllegalStateException("분석 결과의 필수 항목이 비어 있습니다.");
        }
    }

    private void syncTechAndRecommend(Long memberId, AiAnalysisVO analysis,
            JobPostingVO job, LlmAnalysisResponse response) {
        dao.deleteTechs(analysis.getAnalysisId());
        List<String> ownedTechs = dao.selectMemberTechs(memberId);
        Map<String, LlmAnalysisResponse.Tech> unique = new LinkedHashMap<>();
        for (LlmAnalysisResponse.Tech tech : response.getTechnologies()) {
            if (tech == null || blank(tech.getName())) continue;
            String key = canonical(tech.getName());
            if (key.isBlank()) continue;
            LlmAnalysisResponse.Tech old = unique.get(key);
            if (old == null || "REQUIRED".equalsIgnoreCase(tech.getType())) unique.put(key, tech);
        }
        for (LlmAnalysisResponse.Tech tech : unique.values()) {
            AiJobTechVO item = new AiJobTechVO();
            item.setAnalysisId(analysis.getAnalysisId());
            item.setTechName(tech.getName().trim());
            item.setRequirementType("PREFERRED".equalsIgnoreCase(tech.getType()) ? "PREFERRED" : "REQUIRED");
            item.setMatchStatus(bestMatch(tech.getName(), ownedTechs));
            dao.insertTech(item);
        }

        dao.deleteRecommendations(analysis.getAnalysisId());
        List<AiJobTechVO> requiredTechs = dao.selectTechs(analysis.getAnalysisId());
        List<ProjectScore> scores = new ArrayList<>();
        for (ProjectExperienceVO project : projects.list(memberId)) {
            scores.add(scoreProject(project, requiredTechs, job, response));
        }
        scores.sort(Comparator.comparingInt(ProjectScore::score).reversed()
                .thenComparing(x -> Optional.ofNullable(x.project().getEndDate())
                        .orElse(Optional.ofNullable(x.project().getStartDate()).orElse(java.time.LocalDate.MIN)),
                        Comparator.reverseOrder()));
        for (int i = 0; i < Math.min(3, scores.size()); i++) {
            ProjectScore score = scores.get(i);
            ProjectExperienceVO project = score.project();
            AiExperienceRecommendVO recommendation = new AiExperienceRecommendVO();
            recommendation.setAnalysisId(analysis.getAnalysisId());
            recommendation.setProjectId(project.getProjectId());
            recommendation.setRankNo(i + 1);
            recommendation.setReason(score.reason());
            recommendation.setSourceDetail(buildSourceDetail(project));
            recommendation.setSavedYn("N");
            dao.insertRecommendation(recommendation);
        }
    }

    private ProjectScore scoreProject(ProjectExperienceVO project, List<AiJobTechVO> requiredTechs,
            JobPostingVO job, LlmAnalysisResponse response) {
        List<String> projectTechs = dao.selectProjectTechs(project.getProjectId());
        List<String> matches = new ArrayList<>();
        int score = 0;
        for (AiJobTechVO required : requiredTechs) {
            String relation = bestMatch(required.getTechName(), projectTechs);
            boolean mandatory = "REQUIRED".equals(required.getRequirementType());
            if ("OWNED".equals(relation)) {
                score += mandatory ? 20 : 12;
                matches.add(required.getTechName() + "(직접 일치)");
            } else if ("PARTIAL".equals(relation)) {
                score += mandatory ? 10 : 6;
                matches.add(required.getTechName() + "(유사 경험)");
            }
        }
        String evidence = projectText(project).toLowerCase();
        int keywordHits = relevantWords(job, response).stream()
                .mapToInt(word -> evidence.contains(word) ? 1 : 0).sum();
        score += Math.min(25, keywordHits * 5);
        score += Math.min(10, project.getFeatures().size() * 2);
        score += Math.min(10, project.getTroubles().size() * 3);

        String reason;
        if (!matches.isEmpty()) {
            reason = "공고 요구기술과 연결되는 경험: " + String.join(", ", matches)
                    + ". 직무 키워드 " + keywordHits + "개가 프로젝트 수행 내용과 연결됩니다.";
        } else if (keywordHits > 0) {
            reason = "직접 일치 기술은 적지만 프로젝트 역할·수행 내용에서 직무 키워드 "
                    + keywordHits + "개가 확인되어 활용 가능한 경험입니다.";
        } else {
            reason = "직접 기술 일치는 적지만 담당 기능과 문제 해결 과정을 중심으로 보조 경험으로 활용할 수 있습니다.";
        }
        return new ProjectScore(project, score, reason);
    }

    private String buildSourceDetail(ProjectExperienceVO project) {
        String role = blank(project.getRoleName()) ? "역할 미입력" : project.getRoleName();
        String feature = project.getFeatures().stream().map(ProjectFeatureVO::getFeatureName)
                .filter(x -> !blank(x)).limit(3).collect(Collectors.joining(", "));
        String trouble = project.getTroubles().stream().map(ProjectTroubleVO::getTitle)
                .filter(x -> !blank(x)).limit(2).collect(Collectors.joining(", "));
        return "담당 역할: " + role + " / 담당 기능: " + (feature.isBlank() ? "미입력" : feature)
                + " / 문제 해결: " + (trouble.isBlank() ? "미입력" : trouble)
                + " / 자소서 활용: 상황-과제-행동-결과(STAR) 순서로 수치와 본인 기여도를 보강하세요.";
    }

    private void decorate(AiAnalysisVO analysis) {
        analysis.setTechs(dao.selectTechs(analysis.getAnalysisId()));
        analysis.setRecommendations(dao.selectRecommendations(analysis.getAnalysisId()));
        analysis.getTechs().forEach(tech -> tech.setLearningDirection(learningDirection(tech)));
        double earned = 0;
        double maximum = 0;
        for (AiJobTechVO tech : analysis.getTechs()) {
            double weight = "REQUIRED".equals(tech.getRequirementType()) ? 2.0 : 1.0;
            maximum += weight;
            if ("OWNED".equals(tech.getMatchStatus())) earned += weight;
            else if ("PARTIAL".equals(tech.getMatchStatus())) earned += weight * 0.6;
        }
        analysis.setReadinessScore(maximum == 0 ? 0 : (int) Math.round(earned * 100.0 / maximum));
        analysis.setInterviewQuestions(buildInterviewQuestions(analysis));
    }

    private List<String> buildInterviewQuestions(AiAnalysisVO analysis) {
        LinkedHashSet<String> questions = new LinkedHashSet<>();
        questions.add(analysis.getJobTitle() + " 직무에 지원한 이유와 본인의 강점을 구체적인 경험으로 설명해 주세요.");
        analysis.getTechs().stream().filter(x -> !"MISSING".equals(x.getMatchStatus())).limit(4)
                .forEach(x -> questions.add(x.getTechName()
                        + "을(를) 프로젝트 또는 학습에서 사용한 방식, 선택 이유, 결과를 설명해 주세요."));
        analysis.getTechs().stream().filter(x -> "MISSING".equals(x.getMatchStatus())).limit(2)
                .forEach(x -> questions.add(x.getTechName()
                        + " 경험이 부족한 상황에서 입사 전후 어떤 순서로 역량을 확보할 계획인가요?"));
        analysis.getRecommendations().stream().limit(2)
                .forEach(x -> questions.add(x.getProjectName()
                        + " 프로젝트에서 가장 어려웠던 문제의 원인과 본인이 해결한 과정을 설명해 주세요."));
        return new ArrayList<>(questions);
    }

    private String learningDirection(AiJobTechVO tech) {
        String name = tech.getTechName();
        String key = canonical(name);
        String family = TECH_FAMILIES.getOrDefault(key, key);
        String plan = switch (family) {
            case "spring" -> "공식 가이드로 핵심 구조를 익힌 뒤 MVC·Validation·예외처리 API를 구현하고 현재 프로젝트에 적용";
            case "database", "persistence" -> "DDL·JOIN·서브쿼리·트랜잭션을 실습한 뒤 MyBatis Mapper와 실행계획으로 검증";
            case "frontend" -> "문법과 DOM/비동기 통신을 학습한 뒤 폼 검증과 REST 연동 화면을 제작";
            case "container" -> "이미지·컨테이너·네트워크 기초를 익힌 뒤 JOBON WAR 배포 환경을 컨테이너로 재현";
            case "cloud" -> "컴퓨팅·DB·스토리지·권한 기초를 익힌 뒤 테스트 서버 배포와 로그 확인까지 수행";
            case "cicd", "scm" -> "브랜치 전략과 자동 빌드 단계를 정리한 뒤 Maven 테스트·WAR 산출 자동화를 구성";
            default -> name + " 공식 문서의 핵심 개념을 학습하고 작은 예제를 만든 뒤 JOBON 기능에 적용";
        };
        return plan + " → 결과와 시행착오를 성장 기록에 기술 키워드와 함께 저장하세요.";
    }

    private String bestMatch(String required, Collection<String> owned) {
        boolean partial = false;
        for (String candidate : owned) {
            Match relation = relation(required, candidate);
            if (relation == Match.EXACT) return "OWNED";
            if (relation == Match.PARTIAL) partial = true;
        }
        return partial ? "PARTIAL" : "MISSING";
    }

    private Match relation(String left, String right) {
        String a = canonical(left);
        String b = canonical(right);
        if (a.isBlank() || b.isBlank()) return Match.NONE;
        if (a.equals(b)) return Match.EXACT;
        if ((a.length() >= 3 && b.contains(a)) || (b.length() >= 3 && a.contains(b))) return Match.PARTIAL;
        String af = TECH_FAMILIES.get(a);
        String bf = TECH_FAMILIES.get(b);
        return af != null && af.equals(bf) ? Match.PARTIAL : Match.NONE;
    }

    private String canonical(String value) {
        String normalized = value == null ? "" : value.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9가-힣+#]", "");
        return TECH_ALIASES.getOrDefault(normalized, normalized);
    }

    private Set<String> relevantWords(JobPostingVO job, LlmAnalysisResponse response) {
        String source = String.join(" ", safe(job.getTitle()), safe(job.getJobRole()), safe(response.getMainTasks()));
        Set<String> stop = Set.of("담당", "업무", "개발", "관련", "경험", "지원", "위한", "있는", "합니다", "그리고");
        return Arrays.stream(source.toLowerCase().split("[^a-z0-9가-힣+#]+"))
                .filter(x -> x.length() >= 2 && !stop.contains(x)).limit(30)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String projectText(ProjectExperienceVO project) {
        StringBuilder text = new StringBuilder();
        text.append(safe(project.getProjectName())).append(' ').append(safe(project.getRoleName())).append(' ')
                .append(safe(project.getDescription())).append(' ');
        project.getFeatures().forEach(x -> text.append(safe(x.getFeatureName())).append(' ')
                .append(safe(x.getDetail())).append(' '));
        project.getTroubles().forEach(x -> text.append(safe(x.getTitle())).append(' ')
                .append(safe(x.getProblem())).append(' ').append(safe(x.getCause())).append(' ')
                .append(safe(x.getSolution())).append(' ').append(safe(x.getResult())).append(' '));
        return text.toString();
    }

    private String safeMessage(Exception error) {
        if (error == null || blank(error.getMessage())) return "알 수 없는 오류";
        String message = error.getMessage().trim();
        return message.length() > 500 ? message.substring(0, 500) : message;
    }

    private String safe(String value) { return value == null ? "" : value; }
    private boolean blank(String value) { return value == null || value.isBlank(); }
    private enum Match { EXACT, PARTIAL, NONE }
    private record ProjectScore(ProjectExperienceVO project, int score, String reason) { }
}
