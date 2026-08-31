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
        <title>채용공고 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="job" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert--success">${successMessage}</div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert--danger">${errorMessage}</div>
                </c:if>
                <section class="page-heading">
                    <div>
                        <h1>채용공고</h1>
                        <p>관심 공고를 저장하고 지원·TODO·AI 분석과 연결하세요.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/job/new">채용공고 등록</a>
                </section>
                <form class="toolbar"><input class="form-control toolbar__grow" name="keyword"
                        value="${keyword}" placeholder="공고명 또는 기업명"><input class="form-control select-sm"
                        name="jobRole" value="${jobRole}" placeholder="직무"><select
                        class="form-control select-sm" name="sort">
                        <option value="latest">최근 등록순</option>
                        <option value="deadline">마감 임박순</option>
                    </select><button class="jobon-btn jobon-btn--ghost">검색</button></form>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업</th>
                                <th>공고명</th>
                                <th>직무</th>
                                <th>마감일</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${jobs}">
                                <tr>
                                    <td>${x.companyName}</td>
                                    <td><strong>${x.title}</strong></td>
                                    <td>${x.jobRole}</td>
                                    <td>${x.deadline}</td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/job/detail?id=${x.jobId}">상세</a><a
                                                class="text-link" href="${ctx}/job/edit?id=${x.jobId}">수정</a>
                                            <form method="post" action="${ctx}/job/${x.jobId}/delete"
                                                data-confirm="채용공고를 삭제할까요?"><button
                                                    class="text-link danger">삭제</button></form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty jobs}">
                                <tr>
                                    <td colspan="5" class="empty-cell">등록된 채용공고가 없습니다.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>