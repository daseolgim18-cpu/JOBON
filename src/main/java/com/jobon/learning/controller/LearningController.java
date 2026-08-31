package com.jobon.learning.controller;

/** [추가] 성장 기록 CRUD + 기술 키워드 Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.learning.service.LearningRecordService;
import com.jobon.learning.vo.LearningRecordVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/learning")
public class LearningController {
    private final LearningRecordService service;

    public LearningController(LearningRecordService s) {
        service = s;
    }

    @GetMapping("/list")
    String list(HttpSession s, Model m) {
        m.addAttribute("records", service.list(SessionMemberUtil.requireMemberId(s)));
        return "learning/list";
    }

    @GetMapping("/new")
    String form(Model m) {
        m.addAttribute("record", new LearningRecordVO());
        return "learning/new";
    }

    @PostMapping
    String create(@ModelAttribute LearningRecordVO v, HttpSession s) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.create(v);
        return "redirect:/learning/list";
    }

    @GetMapping("/detail")
    String detail(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("record", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "learning/detail";
    }

    @GetMapping("/edit")
    String edit(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("record", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "learning/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @ModelAttribute LearningRecordVO v, HttpSession s) {
        v.setLearningId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.update(v);
        return "redirect:/learning/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        return "redirect:/learning/list";
    }
}
