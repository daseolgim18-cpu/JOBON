package com.jobon.ai.controller;

/** [추가] AI 분석 요청/상세/경험추천/저장 Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jobon.ai.service.AiAnalysisService;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.job.service.JobPostingService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ai")
public class AiAnalysisController {
    private final AiAnalysisService service;
    private final JobPostingService jobs;

    public AiAnalysisController(AiAnalysisService s, JobPostingService j) {
        service = s;
        jobs = j;
    }

    @GetMapping("/analysis")
    String list(HttpSession s, Model m) {
        Long id = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("analyses", service.list(id));
        m.addAttribute("jobs", jobs.list(id, null, null, "latest"));
        return "ai/analysis";
    }

    // [추가] 분석 요청 전 선택한 공고의 입력 데이터 품질 안내를 반환합니다.
    @GetMapping("/analysis/quality")
    @ResponseBody
    java.util.List<String> quality(@RequestParam Long jobId, HttpSession s) {
        return service.dataQualityWarnings(SessionMemberUtil.requireMemberId(s), jobId);
    }

    @PostMapping("/analysis")
    String analyze(@RequestParam Long jobId, HttpSession s) {
        var a = service.analyze(SessionMemberUtil.requireMemberId(s), jobId);
        return "redirect:/ai/analysis/detail?id=" + a.getAnalysisId();
    }

    @GetMapping({ "/analysis/detail", "/job-analysis" })
    String detail(@RequestParam(required = false) Long id, @RequestParam(required = false) Long jobId, HttpSession s,
            Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        var a = id != null ? service.get(mid, id) : service.getByJob(mid, jobId);
        m.addAttribute("analysis", a);
        m.addAttribute("dataQualityWarnings", service.dataQualityWarnings(mid, a.getJobId()));
        return "ai/job-analysis";
    }

    @PostMapping("/analysis/{id}/rerun")
    String rerun(@PathVariable Long id, HttpSession s) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        var old = service.get(mid, id);
        var a = service.analyze(mid, old.getJobId());
        return "redirect:/ai/analysis/detail?id=" + a.getAnalysisId();
    }


    /** [추가] AI 분석 결과 목록에서 선택한 분석 결과를 삭제합니다. */
    @PostMapping("/analysis/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s, RedirectAttributes r) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        r.addFlashAttribute("successMessage", "AI 분석 결과가 삭제되었습니다.");
        return "redirect:/ai/analysis";
    }

    @GetMapping("/experience-recommend")
    String recommend(@RequestParam Long analysisId, HttpSession s, Model m) {
        m.addAttribute("analysis", service.get(SessionMemberUtil.requireMemberId(s), analysisId));
        return "ai/experience-recommend";
    }

    @PostMapping("/recommend/{id}/save")
    String save(@PathVariable Long id, @RequestParam Long analysisId,
            @RequestParam(defaultValue = "true") boolean saved, HttpSession s) {
        service.saveRecommendation(SessionMemberUtil.requireMemberId(s), id, saved);
        return "redirect:/ai/experience-recommend?analysisId=" + analysisId;
    }

    /** [추가] 저장한 자소서 경험 추천 목록을 조회합니다. */
    @GetMapping("/saved-recommendations")
    String savedRecommendations(HttpSession s, Model m) {
        Long memberId = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("savedRecommendations", service.savedRecommendations(memberId));
        return "ai/saved-recommendations";
    }

    /** [추가] 저장 목록에서 저장을 해제하고 목록 화면으로 돌아갑니다. */
    @PostMapping("/recommend/{id}/unsave")
    String unsave(@PathVariable Long id, HttpSession s, RedirectAttributes r) {
        service.saveRecommendation(SessionMemberUtil.requireMemberId(s), id, false);
        r.addFlashAttribute("successMessage", "저장한 경험에서 해제되었습니다.");
        return "redirect:/ai/saved-recommendations";
    }
}
