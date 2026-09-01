package com.jobon.todo.controller;

/** [추가] 할 일 CRUD + 기업/공고/마감일 연계 Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.todo.service.TodoItemService;
import com.jobon.todo.vo.TodoItemVO;
import com.jobon.company.service.CompanyService;
import com.jobon.job.service.JobPostingService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/todo")
public class TodoController {
    private final TodoItemService service;
    private final CompanyService companies;
    private final JobPostingService jobs;

    public TodoController(TodoItemService s, CompanyService c, JobPostingService j) {
        service = s;
        companies = c;
        jobs = j;
    }

    @GetMapping("/list")
    String list(@RequestParam(required = false) String status, HttpSession s, Model m) {
        m.addAttribute("todos", service.list(SessionMemberUtil.requireMemberId(s), status));
        m.addAttribute("status", status);
        return "todo/list";
    }

    @GetMapping("/new")
    String form(@RequestParam(required = false) Long jobId, HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        TodoItemVO v = new TodoItemVO();
        v.setJobId(jobId);
        if (jobId != null) {
            var j = jobs.get(mid, jobId);
            v.setCompanyId(j.getCompanyId());
            v.setDueDate(j.getDeadline());
            v.setTitle(j.getTitle() + " 지원 준비");
        }
        bind(m, mid, v);
        return "todo/new";
    }

    @PostMapping
    String create(@ModelAttribute TodoItemVO v, HttpSession s) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.create(v);
        return "redirect:/todo/list";
    }

    @GetMapping("/edit")
    String edit(@RequestParam Long id, HttpSession s, Model m) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        bind(m, mid, service.get(mid, id));
        return "todo/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @ModelAttribute TodoItemVO v, HttpSession s) {
        v.setTodoId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.update(v);
        return "redirect:/todo/list";
    }

    @PostMapping("/{id}/toggle")
    String toggle(@PathVariable Long id, HttpSession s) {
        Long mid = SessionMemberUtil.requireMemberId(s);
        TodoItemVO v = service.get(mid, id);
        v.setStatus("DONE".equals(v.getStatus()) ? "TODO" : "DONE");
        v.setMemberId(mid);
        service.update(v);
        return "redirect:/todo/list";
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        return "redirect:/todo/list";
    }

    private void bind(Model m, Long mid, TodoItemVO v) {
        m.addAttribute("todo", v);
        m.addAttribute("companies", companies.list(mid, null, null));
        m.addAttribute("jobs", jobs.list(mid, null, null, "deadline"));
    }
}
