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
        <title>AI 분석 | JOBON</title>
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
                        <h1>AI 분석</h1>
                        <p>채용공고 원문에서 업무·자격요건·우대기술을 추출하고 내 경험과 비교합니다.</p>
                    </div>
                </section>
                <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
                <form class="card card--padded ai-request" method="post" action="${ctx}/ai/analysis">
                    <label><span class="form-label">분석할 채용공고</span><select class="form-control" name="jobId"
                            required>
                            <option value="">공고 선택</option>
                            <c:forEach var="j" items="${jobs}">
                                <option value="${j.jobId}">${j.companyName} - ${j.title}</option>
                            </c:forEach>
                        </select></label><button class="jobon-btn jobon-btn--primary">분석 요청</button></form>
                <div class="card table-wrap mt20">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업</th>
                                <th>공고</th>
                                <th>상태</th>
                                <th>분석일</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${analyses}">
                                <tr>
                                    <td>${x.companyName}</td>
                                    <td>${x.jobTitle}</td>
                                    <td><span class="badge ${x.status eq 'FAILED' ? 'badge--red' : 'badge--green'}">${x.status}</span></td>
                                    <td>${x.requestedAt}</td>
                                    <td><a class="text-link"
                                            href="${ctx}/ai/analysis/detail?id=${x.analysisId}">상세 보기</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
