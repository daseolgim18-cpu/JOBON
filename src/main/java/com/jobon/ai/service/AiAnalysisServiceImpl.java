package com.jobon.ai.service;

/** [추가] LLM 분석 결과 저장 → 기술 보유/부분/부족 분류 → 프로젝트 경험 TOP3 추천 → 준비도/면접질문 구성 */
import java.util.*;
import java.util.stream.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jobon.ai.dao.AiAnalysisDAO;
import com.jobon.ai.dto.*;
import com.jobon.ai.vo.*;
import com.jobon.job.service.JobPostingService;
import com.jobon.job.vo.JobPostingVO;
import com.jobon.project.service.ProjectExperienceService;
import com.jobon.project.vo.ProjectExperienceVO;

@Service
public class AiAnalysisServiceImpl implements AiAnalysisService {
    private final AiAnalysisDAO dao;
    private final JobPostingService jobs;
    private final ProjectExperienceService projects;
    private final LlmClient llm;
    private final LocalJobAnalyzer local;

    public AiAnalysisServiceImpl(AiAnalysisDAO d, JobPostingService j, ProjectExperienceService p, LlmClient l,
            LocalJobAnalyzer x) {
        dao = d;
        jobs = j;
        projects = p;
        llm = l;
        local = x;
    }

    public List<AiAnalysisVO> list(Long memberId) {
        return dao.selectList(memberId);
    }

    @Transactional
    public AiAnalysisVO analyze(Long memberId, Long jobId) {
        JobPostingVO job = jobs.get(memberId, jobId);
        AiAnalysisVO a = new AiAnalysisVO();
        a.setMemberId(memberId);
        a.setJobId(jobId);
        a.setStatus("PROCESSING");
        dao.insertAnalysis(a);
        try {
            LlmAnalysisResponse r;
            try {
                r = llm.analyze(LlmAnalysisRequest.builder().title(job.getTitle()).jobRole(job.getJobRole())
                        .originalText(job.getOriginalText()).build());
                a.setModelName("external-llm");
            } catch (Exception ex) {
                r = local.analyze(LlmAnalysisRequest.builder().title(job.getTitle()).jobRole(job.getJobRole())
                        .originalText(job.getOriginalText()).build());
                a.setModelName("local-fallback");
                a.setErrorMessage("외부 LLM 미사용/실패: " + ex.getMessage());
            }
            a.setSummary(r.getSummary());
            a.setMainTasks(r.getMainTasks());
            a.setQualifications(r.getQualifications());
            a.setPreferences(r.getPreferences());
            a.setRequiredCompetencies(r.getRequiredCompetencies());
            a.setRawResponse(r.getRawResponse());
            a.setStatus("COMPLETED");
            dao.completeAnalysis(a);
            syncTechAndRecommend(memberId, a, r);
            return get(memberId, a.getAnalysisId());
        } catch (Exception e) {
            a.setStatus("FAILED");
            a.setErrorMessage(e.getMessage());
            dao.failAnalysis(a);
            throw e;
        }
    }

    public AiAnalysisVO get(Long memberId, Long id) {
        AiAnalysisVO a = dao.selectOne(memberId, id);
        if (a == null)
            throw new IllegalArgumentException("AI 분석 결과를 찾을 수 없습니다.");
        decorate(memberId, a);
        return a;
    }

    public AiAnalysisVO getByJob(Long memberId, Long jobId) {
        AiAnalysisVO a = dao.selectLatestByJob(memberId, jobId);
        if (a == null)
            return analyze(memberId, jobId);
        decorate(memberId, a);
        return a;
    }

    public void saveRecommendation(Long memberId, Long id, boolean saved) {
        dao.updateRecommendationSaved(id, saved ? "Y" : "N");
    }

    private void syncTechAndRecommend(Long memberId, AiAnalysisVO a, LlmAnalysisResponse r) {
        dao.deleteTechs(a.getAnalysisId());
        Set<String> owned = dao.selectMemberTechs(memberId).stream().map(this::norm).collect(Collectors.toSet());
        for (var t : r.getTechnologies()) {
            if (t.getName() == null || t.getName().isBlank())
                continue;
            AiJobTechVO x = new AiJobTechVO();
            x.setAnalysisId(a.getAnalysisId());
            x.setTechName(t.getName().trim());
            x.setRequirementType("PREFERRED".equalsIgnoreCase(t.getType()) ? "PREFERRED" : "REQUIRED");
            String n = norm(t.getName());
            x.setMatchStatus(owned.contains(n) ? "OWNED"
                    : owned.stream().anyMatch(o -> o.contains(n) || n.contains(o)) ? "PARTIAL" : "MISSING");
            dao.insertTech(x);
        }
        dao.deleteRecommendations(a.getAnalysisId());
        List<AiJobTechVO> req = dao.selectTechs(a.getAnalysisId());
        List<ProjectExperienceVO> ps = projects.list(memberId);
        record Score(ProjectExperienceVO p, int s, List<String> m) {
        }
        List<Score> scores = new ArrayList<>();
        for (ProjectExperienceVO p : ps) {
            Set<String> pt = dao.selectProjectTechs(p.getProjectId()).stream().map(this::norm)
                    .collect(Collectors.toSet());
            List<String> m = req.stream().filter(t -> pt.contains(norm(t.getTechName()))).map(AiJobTechVO::getTechName)
                    .toList();
            int score = m.size() * 10 + (p.getDescription() != null && jobWords(a, r, p.getDescription()) ? 5 : 0);
            scores.add(new Score(p, score, m));
        }
        scores.sort(Comparator.comparingInt(Score::s).reversed());
        for (int i = 0; i < Math.min(3, scores.size()); i++) {
            Score s = scores.get(i);
            AiExperienceRecommendVO x = new AiExperienceRecommendVO();
            x.setAnalysisId(a.getAnalysisId());
            x.setProjectId(s.p().getProjectId());
            x.setRankNo(i + 1);
            x.setReason(
                    s.m().isEmpty() ? "직무 경험 서술을 중심으로 활용 가능합니다." : "공고 요구 기술과 연결되는 경험: " + String.join(", ", s.m()));
            x.setSourceDetail("프로젝트 역할: " + Optional.ofNullable(s.p().getRoleName()).orElse("-")
                    + " / 보완 방향: 부족 기술을 학습 기록과 함께 STAR 방식으로 설명하세요.");
            x.setSavedYn("N");
            dao.insertRecommendation(x);
        }
    }

    private boolean jobWords(AiAnalysisVO a, LlmAnalysisResponse r, String d) {
        String x = d.toLowerCase();
        for (String w : Optional.ofNullable(r.getMainTasks()).orElse("").split("\\s+"))
            if (w.length() > 3 && x.contains(w.toLowerCase()))
                return true;
        return false;
    }

    private void decorate(Long memberId, AiAnalysisVO a) {
        a.setTechs(dao.selectTechs(a.getAnalysisId()));
        a.setRecommendations(dao.selectRecommendations(a.getAnalysisId()));
        long total = a.getTechs().size(),
                owned = a.getTechs().stream().filter(t -> "OWNED".equals(t.getMatchStatus())).count(),
                partial = a.getTechs().stream().filter(t -> "PARTIAL".equals(t.getMatchStatus())).count();
        a.setReadinessScore(total == 0 ? 0 : (int) Math.round((owned + partial * .5) * 100.0 / total));
        List<String> q = new ArrayList<>();
        q.add("이 직무에 지원한 이유와 본인의 강점을 설명해 주세요.");
        for (AiJobTechVO t : a.getTechs().stream().limit(4).toList())
            q.add(t.getTechName() + "을(를) 실제 프로젝트에서 어떻게 활용했는지 설명해 주세요.");
        a.getRecommendations().stream().limit(2)
                .forEach(r -> q.add(r.getProjectName() + " 프로젝트에서 가장 어려웠던 문제와 해결 과정을 설명해 주세요."));
        a.setInterviewQuestions(q);
    }

    private String norm(String s) {
        return s == null ? "" : s.toLowerCase().replaceAll("[^a-z0-9가-힣+#.]", "");
    }
}
