package com.jobon.activity.controller;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] 마이페이지 활동 내역 조회 Controller입니다.
 * 전체 활동 또는 활동 유형별 필터링 결과를 실제 ACTIVITY_LOG 데이터로 조회합니다.
 */
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jobon.activity.service.ActivityLogService;
import com.jobon.common.util.SessionMemberUtil;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/mypage/activity")
public class ActivityLogController {
    private final ActivityLogService service;

    public ActivityLogController(ActivityLogService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String type, HttpSession session, Model model) {
        Long memberId = SessionMemberUtil.requireMemberId(session);
        model.addAttribute("activities", service.list(memberId, type));
        model.addAttribute("type", type == null ? "" : type);
        return "mypage/activity";
    }
}
