package com.jobon.job.controller;

/** [추가] 채용공고 CRUD Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.apply.service.ApplicationService;
import com.jobon.company.service.CompanyService;
import com.jobon.job.service.JobPostingService;
import com.jobon.job.vo.JobPostingVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/job")
public class JobPostingController {
    private final JobPostingService service;
    private final CompanyService companyService;
    private final ApplicationService applicationService;

    public JobPostingController(JobPostingService s, CompanyService c, ApplicationService applicationService) {
        service = s;
        companyService = c;
        this.applicationService = applicationService;
    }

    @GetMapping("/list")
    String list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String jobRole,
            @RequestParam(defaultValue = "latest") String sort, HttpSession ss, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(ss);
        m.addAttribute("jobs", service.list(mid, keyword, jobRole, sort));
        m.addAttribute("keyword", keyword);
        m.addAttribute("jobRole", jobRole);
        m.addAttribute("sort", sort);
        return "job/list";
    }

    @GetMapping("/new")
    String form(HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("job", new JobPostingVO());
        m.addAttribute("companies", companyService.list(mid, null, null));
        return "job/new";
    }

    @PostMapping
    String create(@ModelAttribute JobPostingVO v, HttpSession s, RedirectAttributes r) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.create(v);
        r.addFlashAttribute("successMessage", "채용공고가 등록되었습니다.");
        return "redirect:/job/list";
    }

    @GetMapping("/detail")
    String detail(@RequestParam Long id, HttpSession s, Model m) {
        Long memberId = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("job", service.get(memberId, id));
        // [추가] 이미 지원현황이 등록된 공고라면 현재 상태와 상세 이동 버튼을 표시합니다.
        m.addAttribute("currentApplication", applicationService.getByJob(memberId, id));
        return "job/detail";
    }

    @GetMapping("/edit")
    String edit(@RequestParam Long id, HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("job", service.get(mid, id));
        m.addAttribute("companies", companyService.list(mid, null, null));
        return "job/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @ModelAttribute JobPostingVO v, HttpSession s, RedirectAttributes r) {
        v.setJobId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.update(v);
        r.addFlashAttribute("successMessage", "채용공고가 수정되었습니다.");
        return "redirect:/job/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s, RedirectAttributes r) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        r.addFlashAttribute("successMessage", "채용공고가 삭제되었습니다.");
        return "redirect:/job/list";
    }
}
