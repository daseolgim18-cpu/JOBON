package com.jobon.project.controller;

/** [추가] 프로젝트·기능·기술·트러블슈팅 통합 CRUD Controller */
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.project.service.ProjectExperienceService;
import com.jobon.project.vo.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/project")
public class ProjectController {
    private final ProjectExperienceService service;

    public ProjectController(ProjectExperienceService s) {
        service = s;
    }

    @GetMapping("/list")
    String list(HttpSession s, Model m) {
        m.addAttribute("projects", service.list(SessionMemberUtil.requireMemberId(s)));
        return "project/list";
    }

    @GetMapping("/new")
    String form(Model m) {
        m.addAttribute("project", new ProjectExperienceVO());
        return "project/new";
    }

    @PostMapping
    String create(@ModelAttribute ProjectExperienceVO v, @RequestParam(required = false) List<String> featureName,
            @RequestParam(required = false) List<String> featureDetail,
            @RequestParam(required = false) List<String> troubleTitle,
            @RequestParam(required = false) List<String> troubleProblem,
            @RequestParam(required = false) List<String> troubleCause,
            @RequestParam(required = false) List<String> troubleSolution,
            @RequestParam(required = false) List<String> troubleResult, HttpSession s) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        children(v, featureName, featureDetail, troubleTitle, troubleProblem, troubleCause, troubleSolution,
                troubleResult);
        service.create(v);
        return "redirect:/project/list";
    }

    @GetMapping("/detail")
    String detail(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("project", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "project/detail";
    }

    @GetMapping("/edit")
    String edit(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("project", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "project/edit";
    }

    @PostMapping("/{id}")
    String update(@PathVariable Long id, @ModelAttribute ProjectExperienceVO v,
            @RequestParam(required = false) List<String> featureName,
            @RequestParam(required = false) List<String> featureDetail,
            @RequestParam(required = false) List<String> troubleTitle,
            @RequestParam(required = false) List<String> troubleProblem,
            @RequestParam(required = false) List<String> troubleCause,
            @RequestParam(required = false) List<String> troubleSolution,
            @RequestParam(required = false) List<String> troubleResult, HttpSession s) {
        v.setProjectId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        children(v, featureName, featureDetail, troubleTitle, troubleProblem, troubleCause, troubleSolution,
                troubleResult);
        service.update(v);
        return "redirect:/project/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        return "redirect:/project/list";
    }

    private void children(ProjectExperienceVO v, List<String> fn, List<String> fd, List<String> tt, List<String> tp,
            List<String> tc, List<String> ts, List<String> tr) {
        v.setFeatures(new ArrayList<>());
        for (int i = 0; fn != null && i < fn.size(); i++) {
            ProjectFeatureVO x = new ProjectFeatureVO();
            x.setFeatureName(fn.get(i));
            x.setDetail(fd != null && i < fd.size() ? fd.get(i) : null);
            v.getFeatures().add(x);
        }
        v.setTroubles(new ArrayList<>());
        for (int i = 0; tt != null && i < tt.size(); i++) {
            ProjectTroubleVO x = new ProjectTroubleVO();
            x.setTitle(tt.get(i));
            x.setProblem(tp != null && i < tp.size() ? tp.get(i) : null);
            x.setCause(tc != null && i < tc.size() ? tc.get(i) : null);
            x.setSolution(ts != null && i < ts.size() ? ts.get(i) : null);
            x.setResult(tr != null && i < tr.size() ? tr.get(i) : null);
            v.getTroubles().add(x);
        }
    }
}
