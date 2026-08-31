package com.jobon.dashboard.controller;

/** [추가] 대시보드 실제 DB 집계 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.company.service.CompanyService;
import com.jobon.job.service.JobPostingService;
import com.jobon.apply.service.ApplicationService;
import com.jobon.todo.service.TodoItemService;
import com.jobon.ai.service.AiAnalysisService;
import jakarta.servlet.http.HttpSession;

@Controller
public class DashboardController {
    private final CompanyService c;
    private final JobPostingService j;
    private final ApplicationService a;
    private final TodoItemService t;
    private final AiAnalysisService ai;

    public DashboardController(CompanyService c, JobPostingService j, ApplicationService a, TodoItemService t,
            AiAnalysisService ai) {
        this.c = c;
        this.j = j;
        this.a = a;
        this.t = t;
        this.ai = ai;
    }

    @GetMapping("/dashboard")
    String dashboard(HttpSession s, Model m) {
        Long id = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("companies", c.list(id, null, null));
        m.addAttribute("jobs", j.list(id, null, null, "deadline"));
        m.addAttribute("applications", a.list(id, null, null, "latest"));
        m.addAttribute("todos", t.list(id, null));
        m.addAttribute("analyses", ai.list(id));
        return "dashboard/dashboard";
    }
}