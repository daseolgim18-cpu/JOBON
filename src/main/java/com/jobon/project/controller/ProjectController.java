package com.jobon.project.controller;

/** [추가] 프로젝트·기능·기술·트러블슈팅 통합 CRUD Controller */
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.project.service.ProjectExperienceService;
import com.jobon.project.vo.*;
import jakarta.servlet.http.HttpServletRequest;
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
    String create(@ModelAttribute ProjectExperienceVO v, HttpServletRequest request, HttpSession s) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));

        // [수정] 동적 입력 항목은 RequestParam List<String> 자동 변환을 사용하지 않습니다.
        // Spring의 문자열 -> 컬렉션 변환 과정에서 입력 문장 내부의 쉼표(,)가
        // 별도 항목 구분자로 해석될 수 있으므로 Servlet 원본 parameterValues를 그대로 사용합니다.
        children(v,
                parameterValues(request, "featureName"),
                parameterValues(request, "featureDetail"),
                parameterValues(request, "troubleTitle"),
                parameterValues(request, "troubleProblem"),
                parameterValues(request, "troubleCause"),
                parameterValues(request, "troubleSolution"),
                parameterValues(request, "troubleResult"));

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
    String update(@PathVariable Long id, @ModelAttribute ProjectExperienceVO v, HttpServletRequest request,
            HttpSession s) {
        v.setProjectId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));

        // [수정] 등록과 동일하게 수정 시에도 쉼표가 포함된 원문을 그대로 보존합니다.
        children(v,
                parameterValues(request, "featureName"),
                parameterValues(request, "featureDetail"),
                parameterValues(request, "troubleTitle"),
                parameterValues(request, "troubleProblem"),
                parameterValues(request, "troubleCause"),
                parameterValues(request, "troubleSolution"),
                parameterValues(request, "troubleResult"));

        service.update(v);
        return "redirect:/project/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    String delete(@PathVariable Long id, HttpSession s) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        return "redirect:/project/list";
    }

    /**
     * 동일한 name을 가진 동적 폼 입력값을 HTTP 요청에 전달된 원문 그대로 반환합니다.
     * getParameterValues()를 사용하므로 각 textarea 안의 쉼표는 데이터의 일부로 유지됩니다.
     */
    private List<String> parameterValues(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        return values == null ? Collections.emptyList() : Arrays.asList(values);
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
