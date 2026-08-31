<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--=========================================================파일
설명=========================================================회원가입 완료 JSP입니다. 가입 성공 후 로그인 화면 또는 메인 화면으로 이동할 수
있는 완료 화면입니다. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width,initial-scale=1">
        <title>가입 완료 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/member.css">
    </head>

    <body>
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <div class="auth-shell">
                    <div class="card auth-card join-complete">
                        <div class="complete-icon">✓</div>
                        <h1 class="auth-title">회원가입 완료!</h1>
                        <p class="auth-desc">JOBON 회원가입이 완료되었습니다.<br>이제 취업 준비 기록을 시작해보세요.</p><a
                            class="jobon-btn jobon-btn--primary" href="${ctx}/login">로그인하러 가기</a><a
                            class="jobon-btn jobon-btn--ghost" href="${ctx}/main">메인으로 이동</a>
                    </div>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>