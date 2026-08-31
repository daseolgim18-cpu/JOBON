package com.jobon.company.controller;

/** [추가] 기업 등록/목록/상세/수정/삭제 Controller */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.jobon.common.util.SessionMemberUtil;
import com.jobon.company.service.CompanyService;
import com.jobon.company.vo.CompanyVO;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/company")
public class CompanyController {
    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String companyType, HttpSession s, Model m) {
        m.addAttribute("companies", service.list(SessionMemberUtil.requireMemberId(s), keyword, companyType));
        m.addAttribute("keyword", keyword);
        m.addAttribute("companyType", companyType);
        return "company/list";
    }

    @GetMapping("/new")
    public String form(Model m) {
        m.addAttribute("company", new CompanyVO());
        return "company/new";
    }

    @PostMapping
    public String create(@ModelAttribute CompanyVO v, HttpSession s, RedirectAttributes r) {
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.create(v);
        r.addFlashAttribute("successMessage", "기업이 등록되었습니다.");
        return "redirect:/company/list";
    }

    @GetMapping("/detail")
    public String detail(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("company", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "company/detail";
    }

    @GetMapping("/edit")
    public String edit(@RequestParam Long id, HttpSession s, Model m) {
        m.addAttribute("company", service.get(SessionMemberUtil.requireMemberId(s), id));
        return "company/edit";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute CompanyVO v, HttpSession s, RedirectAttributes r) {
        v.setCompanyId(id);
        v.setMemberId(SessionMemberUtil.requireMemberId(s));
        service.update(v);
        r.addFlashAttribute("successMessage", "기업 정보가 수정되었습니다.");
        return "redirect:/company/detail?id=" + id;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession s, RedirectAttributes r) {
        service.delete(SessionMemberUtil.requireMemberId(s), id);
        r.addFlashAttribute("successMessage", "기업이 삭제되었습니다.");
        return "redirect:/company/list";
    }
}
