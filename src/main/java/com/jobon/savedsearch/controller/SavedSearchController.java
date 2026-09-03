package com.jobon.savedsearch.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 마이페이지 저장 검색어 조회/등록/삭제/실행 Controller입니다.
 */
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jobon.common.util.SessionMemberUtil;
import com.jobon.savedsearch.service.SavedSearchService;
import com.jobon.savedsearch.vo.SavedSearchVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage/searches")
public class SavedSearchController {
    private final SavedSearchService service;

    public SavedSearchController(SavedSearchService service) {
        this.service = service;
    }

    @GetMapping
    public String list(HttpSession session, Model model) {
        model.addAttribute("searches", service.list(SessionMemberUtil.requireMemberId(session)));
        return "mypage/searches";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        SavedSearchVO search = new SavedSearchVO();
        search.setTargetType("ALL");
        model.addAttribute("search", search);
        return "mypage/search-new";
    }

    @PostMapping
    public String create(@ModelAttribute SavedSearchVO search, HttpSession session, RedirectAttributes ra) {
        try {
            search.setMemberId(SessionMemberUtil.requireMemberId(session));
            service.create(search);
            ra.addFlashAttribute("successMessage", "검색 조건이 저장되었습니다.");
            return "redirect:/mypage/searches";
        } catch (IllegalArgumentException | IllegalStateException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/mypage/searches/new";
        }
    }

    @GetMapping("/{id}/run")
    public String run(@PathVariable Long id, HttpSession session) {
        SavedSearchVO v = service.get(SessionMemberUtil.requireMemberId(session), id);
        StringBuilder q = new StringBuilder("redirect:/search/result?targetType=").append(enc(v.getTargetType()));
        append(q, "keyword", v.getKeyword());
        append(q, "jobRole", v.getJobRole());
        append(q, "careerType", v.getCareerType());
        append(q, "region", v.getRegion());
        append(q, "postedFrom", v.getPostedFrom() == null ? null : v.getPostedFrom().toString());
        append(q, "postedTo", v.getPostedTo() == null ? null : v.getPostedTo().toString());
        append(q, "deadlineFrom", v.getDeadlineFrom() == null ? null : v.getDeadlineFrom().toString());
        append(q, "deadlineTo", v.getDeadlineTo() == null ? null : v.getDeadlineTo().toString());
        append(q, "extraConditions", v.getExtraConditions());
        return q.toString();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        try {
            service.delete(SessionMemberUtil.requireMemberId(session), id);
            ra.addFlashAttribute("successMessage", "저장된 검색어가 삭제되었습니다.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/mypage/searches";
    }

    private void append(StringBuilder q, String name, String value) {
        if (value != null && !value.isBlank()) q.append('&').append(name).append('=').append(enc(value));
    }

    private String enc(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
    }
}
