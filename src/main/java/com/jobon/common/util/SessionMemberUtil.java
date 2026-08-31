package com.jobon.common.util;

/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * [추가] 로그인 세션에서 MEMBER_ID를 안전하게 꺼내는 공통 유틸리티입니다.
 * 일반 로그인과 SNS 로그인 모두 loginMemberId 또는 loginMember를 사용하므로
 * 도메인별 Controller에서 같은 코드를 반복하지 않도록 분리했습니다.
 */
import com.jobon.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;

public final class SessionMemberUtil {
    private SessionMemberUtil() {}

    public static Long requireMemberId(HttpSession session) {
        Object value = session.getAttribute("loginMemberId");
        if (value instanceof Number number) return number.longValue();
        if (session.getAttribute("loginMember") instanceof MemberVO member && member.getMemberId() != null) {
            return member.getMemberId();
        }
        throw new IllegalStateException("로그인이 필요한 서비스입니다.");
    }
}
