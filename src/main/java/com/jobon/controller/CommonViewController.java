package com.jobon.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * JOBON의 공통 화면 이동용 Controller입니다.
 * 회원/인증 Controller와 URL이 겹치지 않도록 /login, /join 등 회원 전용 매핑은 분리했습니다.
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 공통 레이아웃/화면 틀 확인용 임시 View Controller.
 * 각 도메인의 실제 Controller 구현이 시작되면 해당 경로를 도메인 Controller로 옮기고
 * 이 클래스의 중복 매핑은 삭제하세요.
 */
@Controller
public class CommonViewController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    @GetMapping("/company/list")
    public String companyList() {
        return "company/list";
    }

    @GetMapping("/company/new")
    public String companyNew() {
        return "company/new";
    }

    @GetMapping("/company/detail")
    public String companyDetail() {
        return "company/detail";
    }

    @GetMapping("/company/edit")
    public String companyEdit() {
        return "company/edit";
    }

    @GetMapping("/job/list")
    public String jobList() {
        return "job/list";
    }

    @GetMapping("/job/new")
    public String jobNew() {
        return "job/new";
    }

    @GetMapping("/job/detail")
    public String jobDetail() {
        return "job/detail";
    }

    @GetMapping("/job/edit")
    public String jobEdit() {
        return "job/edit";
    }

    @GetMapping("/apply/list")
    public String applyList() {
        return "apply/list";
    }

    @GetMapping("/apply/detail")
    public String applyDetail() {
        return "apply/detail";
    }

    @GetMapping("/apply/edit")
    public String applyEdit() {
        return "apply/edit";
    }

    @GetMapping("/todo/list")
    public String todoList() {
        return "todo/list";
    }

    @GetMapping("/todo/new")
    public String todoNew() {
        return "todo/new";
    }

    @GetMapping("/todo/edit")
    public String todoEdit() {
        return "todo/edit";
    }

    @GetMapping("/learning/list")
    public String learningList() {
        return "learning/list";
    }

    @GetMapping("/learning/new")
    public String learningNew() {
        return "learning/new";
    }

    @GetMapping("/learning/detail")
    public String learningDetail() {
        return "learning/detail";
    }

    @GetMapping("/learning/edit")
    public String learningEdit() {
        return "learning/edit";
    }

    @GetMapping("/project/list")
    public String projectList() {
        return "project/list";
    }

    @GetMapping("/project/new")
    public String projectNew() {
        return "project/new";
    }

    @GetMapping("/project/detail")
    public String projectDetail() {
        return "project/detail";
    }

    @GetMapping("/project/edit")
    public String projectEdit() {
        return "project/edit";
    }

    @GetMapping("/ai/analysis")
    public String aiAnalysis() {
        return "ai/analysis";
    }

    @GetMapping("/ai/job-analysis")
    public String aiJobAnalysis() {
        return "ai/job-analysis";
    }

    @GetMapping("/ai/experience-recommend")
    public String aiExperienceRecommend() {
        return "ai/experience-recommend";
    }

    @GetMapping({ "/mypage", "/mypage/profile" })
    public String mypage() {
        return "mypage/profile";
    }

    @GetMapping("/mypage/profile/edit")
    public String profileEdit() {
        return "mypage/profile-edit";
    }

    @GetMapping("/mypage/password")
    public String password() {
        return "mypage/password";
    }

    @GetMapping("/mypage/accounts")
    public String accounts() {
        return "mypage/accounts";
    }

    @GetMapping("/mypage/activity")
    public String activity() {
        return "mypage/activity";
    }

    @GetMapping("/mypage/searches")
    public String searches() {
        return "mypage/searches";
    }

    @GetMapping("/mypage/searches/new")
    public String searchNew() {
        return "mypage/search-new";
    }

    @GetMapping("/search/result")
    public String searchResult() {
        return "search/result";
    }
}