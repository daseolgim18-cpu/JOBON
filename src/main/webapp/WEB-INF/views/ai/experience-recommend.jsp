<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- [수정] 스토리보드/ERD 기준 실제 DB 연동 CRUD 및 화면 동작을 적용했습니다. -->
<!doctype html>
<html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>자소서 경험 TOP3 추천 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="ai" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>자소서 활용 프로젝트 경험 TOP3</h1>
                        <p>${analysis.companyName} · ${analysis.jobTitle}</p>
                    </div>
                </section>
                <div class="recommend-list">
                    <c:forEach var="r" items="${analysis.recommendations}">
                        <article class="card card--padded recommend-card">
                            <div class="rank">${r.rankNo}</div>
                            <div>
                                <h3>${r.projectName}</h3>
                                <p>${r.reason}</p>
                                <p class="muted">${r.sourceDetail}</p>
                            </div>
                            <form method="post" action="${ctx}/ai/recommend/${r.recommendId}/save"><input
                                    type="hidden" name="analysisId" value="${analysis.analysisId}"><input
                                    type="hidden" name="saved" value="${r.savedYn ne 'Y'}"><button
                                    class="jobon-btn ${r.savedYn eq 'Y'?'jobon-btn--soft':'jobon-btn--primary'}">${r.savedYn
                                    eq 'Y'?'저장됨':'저장하기'}</button></form>
                        </article>
                    </c:forEach>
                    <c:if test="${empty analysis.recommendations}">
                        <div class="card card--padded empty-panel">등록된 프로젝트 경험이 없어 추천할 수 없습니다. 먼저 프로젝트 경험을
                            등록해주세요.</div>
                    </c:if>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>