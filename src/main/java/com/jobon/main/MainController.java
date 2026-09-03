package com.jobon.main;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [수정] 메인 화면의 대시보드 미리보기를 로그인 사용자의 실제 DB 데이터와 연결합니다.
 * 비로그인 상태에서는 임의의 예시 숫자를 노출하지 않고 로그인 안내 화면을 표시합니다.
 */
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jobon.activity.service.ActivityLogService;
import com.jobon.activity.vo.ActivityLogVO;
import com.jobon.apply.service.ApplicationService;
import com.jobon.apply.vo.ApplicationVO;
import com.jobon.member.vo.MemberVO;
import com.jobon.todo.service.TodoItemService;
import com.jobon.todo.vo.TodoItemVO;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

    private final TodoItemService todoItemService;
    private final ApplicationService applicationService;
    private final ActivityLogService activityLogService;

    public MainController(TodoItemService todoItemService,
            ApplicationService applicationService,
            ActivityLogService activityLogService) {
        this.todoItemService = todoItemService;
        this.applicationService = applicationService;
        this.activityLogService = activityLogService;
    }

    @GetMapping({ "/", "/main" })
    public String main(HttpSession session, Model model) {
        Long memberId = resolveMemberId(session);

        // [추가] 비로그인 사용자는 개인 데이터 대신 로그인 안내형 미리보기를 표시합니다.
        if (memberId == null) {
            model.addAttribute("mainPreviewLoggedIn", false);
            return "main";
        }

        model.addAttribute("mainPreviewLoggedIn", true);

        List<TodoItemVO> todos = todoItemService.list(memberId, null);
        List<ApplicationVO> applications = applicationService.list(memberId, null, null, "latest");
        List<ActivityLogVO> recentActivities = activityLogService.recent(memberId, 1);

        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);

        long todayTodoCount = todos.stream()
                .filter(todo -> !"DONE".equals(todo.getStatus()))
                .filter(todo -> today.equals(todo.getDueDate()))
                .count();

        long doingTodoCount = todos.stream()
                .filter(todo -> "DOING".equals(todo.getStatus()))
                .count();

        long imminentTodoCount = todos.stream()
                .filter(todo -> !"DONE".equals(todo.getStatus()))
                .filter(todo -> todo.getDueDate() != null)
                .filter(todo -> !todo.getDueDate().isBefore(today))
                .filter(todo -> !todo.getDueDate().isAfter(weekEnd))
                .count();

        List<TodoItemVO> weekTodos = todos.stream()
                .filter(todo -> !"DONE".equals(todo.getStatus()))
                .filter(todo -> todo.getDueDate() != null)
                .filter(todo -> !todo.getDueDate().isBefore(today))
                .filter(todo -> !todo.getDueDate().isAfter(weekEnd))
                .limit(3)
                .toList();

        Map<String, Long> statusCounts = new LinkedHashMap<>();
        for (String status : List.of("INTEREST", "APPLIED", "DOCUMENT", "CODING_TEST", "INTERVIEW", "OFFER", "REJECTED")) {
            statusCounts.put(status,
                    applications.stream().filter(application -> status.equals(application.getStatus())).count());
        }

        long applicationTotal = applications.size();

        model.addAttribute("todayTodoCount", todayTodoCount);
        model.addAttribute("doingTodoCount", doingTodoCount);
        model.addAttribute("imminentTodoCount", imminentTodoCount);
        model.addAttribute("weekTodos", weekTodos);
        model.addAttribute("applicationStatusCounts", statusCounts);
        model.addAttribute("applicationTotal", applicationTotal);
        model.addAttribute("applicationDonutStyle", buildApplicationDonutStyle(statusCounts, applicationTotal));
        model.addAttribute("recentActivity", recentActivities.isEmpty() ? null : recentActivities.get(0));

        return "main";
    }

    /** [추가] 메인은 비로그인 접근도 허용하므로 requireMemberId 대신 선택적으로 회원 ID를 확인합니다. */
    private Long resolveMemberId(HttpSession session) {
        Object memberId = session.getAttribute("loginMemberId");
        if (memberId instanceof Number number) {
            return number.longValue();
        }

        Object loginMember = session.getAttribute("loginMember");
        if (loginMember instanceof MemberVO member && member.getMemberId() != null) {
            return member.getMemberId();
        }

        return null;
    }

    /**
     * [추가] APPLICATION의 실제 상태별 건수를 이용하여 메인 미리보기 도넛 차트를 구성합니다.
     * DB에는 상태 코드 원본을 그대로 유지하고 화면 표현만 동적으로 계산합니다.
     */
    private String buildApplicationDonutStyle(Map<String, Long> statusCounts, long total) {
        if (total <= 0) {
            return "background: conic-gradient(#edf1f5 0 100%);";
        }

        String[] statuses = { "INTEREST", "APPLIED", "DOCUMENT", "CODING_TEST", "INTERVIEW", "OFFER", "REJECTED" };
        String[] colors = { "#94a3b8", "#10c96f", "#3b82f6", "#06b6d4", "#8b5cf6", "#f59e0b", "#ef4444" };

        StringBuilder gradient = new StringBuilder("background: conic-gradient(");
        double start = 0.0;

        for (int i = 0; i < statuses.length; i++) {
            double end = start + (statusCounts.getOrDefault(statuses[i], 0L) * 100.0 / total);
            if (i > 0) {
                gradient.append(", ");
            }
            gradient.append(colors[i])
                    .append(' ')
                    .append(String.format(java.util.Locale.US, "%.2f%% %.2f%%", start, end));
            start = end;
        }

        gradient.append(");");
        return gradient.toString();
    }
}
