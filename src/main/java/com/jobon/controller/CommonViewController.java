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
 *
 * [수정] 기업/채용공고/지원/TODO/성장기록/프로젝트/AI/대시보드 기능이 실제 Controller로
 * 구현되어 기존 임시 매핑을 제거했습니다. 아직 전용 Controller가 없는 공통 화면만 유지합니다.
 */
@Controller
public class CommonViewController {
    @GetMapping("/mypage/password")
    public String password() {
        return "mypage/password";
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
