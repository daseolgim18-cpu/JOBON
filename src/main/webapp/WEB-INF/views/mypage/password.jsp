<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>비밀번호 변경 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
</head>
<body>
    <c:set var="activeMenu" value="" scope="request" />
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
        <div class="jobon-container">
            <section class="page-heading">
                <div><h1>비밀번호 변경</h1><p>현재 비밀번호를 확인한 뒤 새 비밀번호로 변경합니다.</p></div>
            </section>
            <c:if test="${not empty successMessage}"><div class="alert alert--success">${successMessage}</div></c:if>
            <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
            <div class="mypage-layout">
                <aside class="card side-nav">
                    <a href="${ctx}/mypage">내 프로필</a>
                    <a href="${ctx}/mypage/password">비밀번호 변경</a>
                    <a href="${ctx}/mypage/accounts">연동 계정</a>
                    <a href="${ctx}/mypage/activity">활동 내역</a>
                    <a href="${ctx}/mypage/searches">저장된 검색어</a>
                    <a href="${ctx}/mypage/withdraw">회원 탈퇴</a>
                </aside>
                <section class="card card--padded">
                    <c:choose>
                        <c:when test="${hasLocalPassword}">
                            <form class="auth-form" method="post" action="${ctx}/mypage/password">
                                <div><label class="form-label">현재 비밀번호</label><input class="form-control" name="currentPassword" type="password" autocomplete="current-password" required></div>
                                <div><label class="form-label">새 비밀번호</label><input class="form-control" name="newPassword" type="password" autocomplete="new-password" required minlength="8" maxlength="20"><p class="form-help">영문, 숫자, 특수문자를 포함한 8~20자</p></div>
                                <div><label class="form-label">새 비밀번호 확인</label><input class="form-control" name="newPasswordConfirm" type="password" autocomplete="new-password" required minlength="8" maxlength="20"></div>
                                <div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage">취소</a><button class="jobon-btn jobon-btn--primary" type="submit">변경하기</button></div>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <div class="empty-panel"><strong>일반 로그인 비밀번호가 없는 SNS 전용 계정입니다.</strong><p>현재 계정은 연동된 SNS 로그인으로 이용해주세요.</p></div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
