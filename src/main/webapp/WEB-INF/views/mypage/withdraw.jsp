<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>회원 탈퇴 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
        <div class="jobon-container">
            <section class="page-heading"><div><h1>회원 탈퇴</h1><p>탈퇴하면 해당 계정으로 JOBON을 이용할 수 없습니다.</p></div></section>
            <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
            <div class="mypage-layout">
                <aside class="card side-nav">
                    <a href="${ctx}/mypage">내 프로필</a><a href="${ctx}/mypage/password">비밀번호 변경</a>
                    <a href="${ctx}/mypage/accounts">연동 계정</a><a href="${ctx}/mypage/activity">활동 내역</a>
                    <a href="${ctx}/mypage/searches">저장된 검색어</a><a href="${ctx}/mypage/withdraw">회원 탈퇴</a>
                </aside>
                <section class="card card--padded">
                    <div class="alert alert--danger">취업 준비 기록은 데이터 무결성을 위해 즉시 물리 삭제하지 않고 계정 상태를 탈퇴 상태로 변경합니다.</div>
                    <form class="auth-form" method="post" action="${ctx}/mypage/withdraw" data-confirm="정말 회원 탈퇴를 진행할까요?">
                        <c:if test="${hasLocalPassword}"><div><label class="form-label">현재 비밀번호</label><input class="form-control" type="password" name="currentPassword" required autocomplete="current-password"></div></c:if>
                        <div><label class="form-label">확인 문구</label><input class="form-control" type="text" name="confirmText" placeholder="탈퇴합니다" required><p class="form-help">확인 문구에 <strong>탈퇴합니다</strong>를 정확히 입력해주세요.</p></div>
                        <div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage">취소</a><button class="jobon-btn jobon-btn--danger" type="submit">회원 탈퇴</button></div>
                    </form>
                </section>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="${ctx}/js/jobon-crud.js"></script>
</body>
</html>
