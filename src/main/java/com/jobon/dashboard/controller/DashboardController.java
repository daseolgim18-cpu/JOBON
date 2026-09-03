package com.jobon.dashboard.controller;

/** [수정] 대시보드 실제 DB 집계 및 취업 준비 정보 통합 */
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.company.service.CompanyService;
import com.jobon.job.service.JobPostingService;
import com.jobon.apply.service.ApplicationService;
import com.jobon.todo.service.TodoItemService;
import com.jobon.ai.service.AiAnalysisService;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.learning.service.LearningRecordService;
import com.jobon.project.service.ProjectExperienceService;
import com.jobon.ai.vo.AiAnalysisVO;
import com.jobon.apply.vo.ApplicationVO;
import com.jobon.job.vo.JobPostingVO;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    private final CompanyService c;
    private final JobPostingService j;
    private final ApplicationService a;
    private final TodoItemService t;
    private final AiAnalysisService ai;
    private final LearningRecordService learning;
    private final ProjectExperienceService projects;
    private final ActivityLogService activities;

    public DashboardController(CompanyService c, JobPostingService j, ApplicationService a, TodoItemService t,
            AiAnalysisService ai, LearningRecordService learning,
            ProjectExperienceService projects, ActivityLogService activities) {
        this.c = c;
        this.j = j;
        this.a = a;
        this.t = t;
        this.ai = ai;
        this.learning = learning;
        this.projects = projects;
        this.activities = activities;
    }

    @GetMapping("/dashboard")
    String dashboard(HttpSession s, Model m) {
        Long id = SessionMemberUtil.requireMemberId(s);
        var companies = c.list(id, null, null);
        var jobs = j.list(id, null, null, "deadline");
        var applications = a.list(id, null, null, "latest");
        var todos = t.list(id, null);
        var analyses = ai.list(id);
        var learningRecords = learning.list(id);
        var projectRecords = projects.list(id);

        LocalDate today = LocalDate.now();
        List<JobPostingVO> imminentJobs = jobs.stream()
                .filter(job -> job.getDeadline() != null && !job.getDeadline().isBefore(today)
                        && !job.getDeadline().isAfter(today.plusDays(7)))
                .peek(job -> job.setDaysUntilDeadline((int) ChronoUnit.DAYS.between(today, job.getDeadline())))
                .limit(5)
                .toList();

        List<ApplicationVO> upcomingSchedules = a.list(id, null, null, "schedule").stream()
                .filter(application -> application.getNextScheduleAt() != null
                        && !application.getNextScheduleAt().isBefore(LocalDateTime.now()))
                .limit(5)
                .toList();

        Map<String, Long> applicationStatusCounts = new LinkedHashMap<>();
        for (String status : List.of("INTEREST", "APPLIED", "DOCUMENT", "CODING_TEST", "INTERVIEW", "OFFER", "REJECTED")) {
            applicationStatusCounts.put(status,
                    applications.stream().filter(x -> status.equals(x.getStatus())).count());
        }

        AiAnalysisVO latestAnalysis = analyses.stream()
                .filter(x -> "COMPLETED".equals(x.getStatus()))
                .findFirst()
                .map(x -> ai.get(id, x.getAnalysisId()))
                .orElse(null);

        m.addAttribute("companies", companies);
        m.addAttribute("jobs", jobs);
        m.addAttribute("applications", applications);
        m.addAttribute("todos", todos);
        m.addAttribute("analyses", analyses);
        m.addAttribute("imminentJobs", imminentJobs);
        m.addAttribute("upcomingSchedules", upcomingSchedules);
        // [수정] TODO/진행중 항목을 모두 대시보드에서 바로 완료 처리할 수 있게 노출합니다.
        m.addAttribute("pendingTodos", todos.stream()
                .filter(todo -> !"DONE".equals(todo.getStatus()))
                .limit(5)
                .toList());
        m.addAttribute("learningRecords", learningRecords.stream().limit(5).toList());
        m.addAttribute("projectRecords", projectRecords.stream().limit(5).toList());
        m.addAttribute("recentActivities", activities.recent(id, 7));
        m.addAttribute("applicationStatusCounts", applicationStatusCounts);
        m.addAttribute("latestAnalysis", latestAnalysis);
        return "dashboard/dashboard";
    }
}
