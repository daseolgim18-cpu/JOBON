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
        <title>지원 현황 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="apply" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>지원 현황</h1>
                        <p>공고별 지원 상태와 다음 일정을 관리하세요.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/apply/new">지원 등록</a>
                </section>
                <form class="toolbar"><input class="form-control toolbar__grow" name="keyword"
                        value="${keyword}" placeholder="기업명"><select class="form-control select-sm"
                        name="status">
                        <option value="">전체 상태</option>
                        <option value="APPLIED">지원완료</option>
                        <option value="DOCUMENT">서류</option>
                        <option value="INTERVIEW">면접</option>
                        <option value="OFFER">합격</option>
                        <option value="REJECTED">불합격</option>
                    </select><button class="jobon-btn jobon-btn--ghost">검색</button></form>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업</th>
                                <th>공고</th>
                                <th>지원일</th>
                                <th>상태</th>
                                <th>다음 일정</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${applications}">
                                <tr>
                                    <td>${x.companyName}</td>
                                    <td>${x.jobTitle}</td>
                                    <td>${x.appliedDate}</td>
                                    <td><span class="badge badge--green">${x.status}</span></td>
                                    <td>${x.nextScheduleAt}</td>
                                    <td><a class="text-link"
                                            href="${ctx}/apply/detail?id=${x.applicationId}">상세</a></td>
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