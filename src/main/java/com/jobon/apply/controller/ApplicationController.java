package com.jobon.apply.controller;

/** [추가] 지원현황 등록/공고연계/상태/메모/필터 Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jobon.apply.service.ApplicationService;
import com.jobon.apply.vo.ApplicationVO;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.job.service.JobPostingService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/apply")
public class ApplicationController {
    private final ApplicationService service;
    private final JobPostingService jobs;

    public ApplicationController(ApplicationService s, JobPostingService j) {
        service = s;
        jobs = j;
    }

    @GetMapping("/list")
    String list(@RequestParam(required = false) String keyword, @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "latest") String sort, HttpSession s, Model m) {
        m.addAttribute("applications", service.list(SessionMemberUtil.requireMemberId(s), keyword, status, sort));
        m.addAttribute("keyword", keyword);
        m.addAttribute("status", status);
        return "apply/list";
    }

    @GetMapping("/new")
    String form(@RequestParam(required = false) Long jobId, HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        ApplicationVO v = new ApplicationVO();
        v.setJobId(jobId);
        v.setStatus("INTEREST");
        m.addAttribute("application", v);
        m.addAttribute("jobs", jobs.list(mid, null, null, "latest"));
        return "apply/edit";
    }

    @PostMapping
    String create(@ModelAttribute ApplicationVO v, HttpSession s, RedirectAttributes r) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.create(v);
        r.addFlashAttribute("successMessage", "지원 현황이 등록되었습니다.");
        return "redirect:/apply/list";
    }

    @GetMapping("/detail")
    String detail(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("application", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "apply/detail";
    }

    @GetMapping("/edit")
    String edit(@RequestParam Long id, HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        m.addAttribute("application", service.get(mid, id));
        m.addAttribute("jobs", jobs.list(mid, null, null, "latest"));
        return "apply/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @ModelAttribute ApplicationVO v, HttpSession s, RedirectAttributes r) {
        v.setApplicationId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.update(v);
        r.addFlashAttribute("successMessage", "지원 현황이 수정되었습니다.");
        return "redirect:/apply/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        return "redirect:/apply/list";
    }
}
