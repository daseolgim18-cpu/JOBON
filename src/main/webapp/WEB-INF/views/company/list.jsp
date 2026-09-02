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
        <title>기업 관리 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css?v=20260902-3">
    </head>

    <body>
        <c:set var="activeMenu" value="company" scope="request" />
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
                        <h1>기업 관리</h1>
                        <p>관심 기업을 등록하고 채용공고와 연결해 관리하세요.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/company/new">기업 등록</a>
                </section>
                <form class="toolbar" method="get"><input class="form-control toolbar__grow" name="keyword"
                        value="${keyword}" placeholder="기업명 검색"><select class="form-control select-sm"
                        name="companyType">
                        <option value="">전체 구분</option>
                        <option value="대기업">대기업</option>
                        <option value="중견기업">중견기업</option>
                        <option value="중소기업">중소기업</option>
                        <option value="스타트업">스타트업</option>
                    </select><button class="jobon-btn jobon-btn--ghost">검색</button></form>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업명</th>
                                <th>구분</th>
                                <th>산업 / 직무</th>
                                <th>등록일</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${companies}">
                                <tr>
                                    <td><strong>${x.companyName}</strong></td>
                                    <td>${x.companyType}</td>
                                    <td>${x.industry} · ${x.jobField}</td>
                                    <td class="date-cell">${x.createdAtLabel}</td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/company/detail?id=${x.companyId}">상세</a><a
                                                class="text-link"
                                                href="${ctx}/company/edit?id=${x.companyId}">수정</a>
                                            <form method="post" action="${ctx}/company/${x.companyId}/delete"
                                                data-confirm="기업을 삭제할까요?"><button
                                                    class="text-link danger">삭제</button></form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty companies}">
                                <tr>
                                    <td colspan="5" class="empty-cell">등록된 기업이 없습니다.</td>
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