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
        <title>대시보드 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="dashboard" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>대시보드</h1>
                        <p>취업 준비 흐름을 한눈에 확인하세요.</p>
                    </div>
                </section>
                <div class="metric-grid">
                    <div class="metric card"><strong>${companies.size()}</strong><span>기업</span></div>
                    <div class="metric card"><strong>${jobs.size()}</strong><span>채용공고</span></div>
                    <div class="metric card"><strong>${applications.size()}</strong><span>지원현황</span></div>
                    <div class="metric card"><strong>${todos.size()}</strong><span>TODO</span></div>
                    <div class="metric card"><strong>${analyses.size()}</strong><span>AI 분석</span></div>
                </div>
                <div class="analysis-grid mt20">
                    <section class="card card--padded">
                        <div class="nested-head">
                            <h3>마감 임박 공고</h3><a class="text-link" href="${ctx}/job/list?sort=deadline">전체 보기</a>
                        </div>
                        <c:forEach var="j" items="${jobs}" end="4">
                            <p><a href="${ctx}/job/detail?id=${j.jobId}">${j.companyName} · ${j.title}</a> <span
                                    class="muted">${j.deadline}</span></p>
                        </c:forEach>
                    </section>
                    <section class="card card--padded">
                        <div class="nested-head">
                            <h3>TODO</h3><a class="text-link" href="${ctx}/todo/list">전체 보기</a>
                        </div>
                        <c:forEach var="t" items="${todos}" end="4">
                            <p>${t.title} <span class="muted">${t.dueDate} · ${t.status}</span></p>
                        </c:forEach>
                    </section>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>