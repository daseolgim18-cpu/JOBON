package com.jobon.common.interceptor;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 로그인 세션 검사 Interceptor입니다.
 * 보호된 화면에 접근할 때 session의 loginMember 존재 여부를 확인하고,
 * 비로그인 상태이면 원래 요청 주소를 저장한 뒤 /login으로 이동시킵니다.
 */
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    // Controller 실행 전에 호출되어 로그인 세션이 있는지 먼저 확인한다.
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 기존 세션에 loginMember가 있으면 이미 로그인된 사용자이므로 요청을 계속 진행한다.
        if (request.getSession(false) != null && request.getSession(false).getAttribute("loginMember") != null)
            return true;
        // 로그인 후 원래 보려던 화면으로 돌아갈 수 있도록 현재 요청 주소를 저장한다.
        String target = request.getRequestURI();
        String query = request.getQueryString();
        if (query != null && !query.isBlank())
            target += "?" + query;
        request.getSession(true).setAttribute("redirectAfterLogin", target);
        // 인증되지 않은 사용자는 로그인 화면으로 이동시킨다.
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}