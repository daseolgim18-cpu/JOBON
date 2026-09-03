<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>저장된 검색어 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
        <div class="jobon-container">
            <section class="page-heading"><div><h1>저장된 검색어</h1><p>자주 사용하는 검색 조건을 저장하고 다시 실행하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/mypage/searches/new">새 검색어 저장</a></section>
            <c:if test="${not empty successMessage}"><div class="alert alert--success">${successMessage}</div></c:if>
            <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
            <div class="mypage-layout">
                <aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a><a href="${ctx}/mypage/withdraw">회원 탈퇴</a></aside>
                <section class="card table-wrap">
                    <table class="data-table">
                        <thead><tr><th>검색 이름</th><th>대상</th><th>조건</th><th>저장일</th><th>관리</th></tr></thead>
                        <tbody>
                            <c:forEach var="s" items="${searches}">
                                <tr><td><strong>${s.searchName}</strong></td><td>${s.targetTypeLabel}</td><td>${s.conditionSummary}</td><td>${s.createdAtLabel}</td><td><div class="table-actions table-actions--nowrap"><a class="text-link" href="${ctx}/mypage/searches/${s.searchId}/run">검색</a><form method="post" action="${ctx}/mypage/searches/${s.searchId}/delete" data-confirm="저장된 검색어를 삭제할까요?"><button class="text-link danger" type="submit">삭제</button></form></div></td></tr>
                            </c:forEach>
                            <c:if test="${empty searches}"><tr><td colspan="5" class="empty-cell">저장된 검색어가 없습니다.</td></tr></c:if>
                        </tbody>
                    </table>
                </section>
            </div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="${ctx}/js/jobon-crud.js"></script>
</body>
</html>
