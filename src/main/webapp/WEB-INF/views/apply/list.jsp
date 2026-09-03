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
        <link rel="stylesheet" href="${ctx}/css/domain.css?v=20260902-3">
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
                        value="${keyword}" placeholder="기업명"><input type="hidden" name="view" value="${view}"><select class="form-control select-sm"
                        name="status">
                        <option value="">전체 상태</option>
                        <option value="INTEREST" ${status eq 'INTEREST' ? 'selected' : ''}>관심</option>
                        <option value="APPLIED" ${status eq 'APPLIED' ? 'selected' : ''}>지원완료</option>
                        <option value="DOCUMENT" ${status eq 'DOCUMENT' ? 'selected' : ''}>서류</option>
                        <option value="CODING_TEST" ${status eq 'CODING_TEST' ? 'selected' : ''}>코딩테스트</option>
                        <option value="INTERVIEW" ${status eq 'INTERVIEW' ? 'selected' : ''}>면접</option>
                        <option value="OFFER" ${status eq 'OFFER' ? 'selected' : ''}>합격</option>
                        <option value="REJECTED" ${status eq 'REJECTED' ? 'selected' : ''}>불합격</option>
                    </select><button class="jobon-btn jobon-btn--ghost">검색</button></form>
                <!-- [추가] 같은 데이터를 목록형 또는 상태별 보드형으로 전환합니다. -->
                <div class="chip-row">
                    <a class="chip ${view eq 'list' ? 'is-active' : ''}" href="${ctx}/apply/list?view=list">목록 보기</a>
                    <a class="chip ${view eq 'board' ? 'is-active' : ''}" href="${ctx}/apply/list?view=board">보드 보기</a>
                </div>
                <c:if test="${view eq 'list'}"><div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업</th>
                                <th>공고</th>
                                <th>지원일</th>
                                <th>상태</th>
                                <th>다음 일정</th>
                                <%-- [수정] 일정 날짜와 D-Day를 별도 열로 분리해 가독성을 높입니다. --%>
                                <th class="dday-column">D-Day</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${applications}">
                                <tr>
                                    <td>${x.companyName}</td>
                                    <td>${x.jobTitle}</td>
                                    <td>${x.appliedDateLabel}</td>
                                    <td><span class="badge badge--green">${x.statusLabel}</span></td>
                                    <td class="date-cell">${empty x.nextScheduleAt ? '-' : x.nextScheduleAtLabel}</td>
                                    <td class="dday-cell"><c:if test="${not empty x.nextScheduleAt}"><span class="deadline-badge">${x.scheduleDdayLabel}</span></c:if><c:if test="${empty x.nextScheduleAt}">-</c:if></td>
                                    <td><a class="text-link"
                                            href="${ctx}/apply/detail?id=${x.applicationId}">상세</a></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div></c:if>
                <c:if test="${view eq 'board'}">
                    <div class="application-board">
                        <c:forEach var="boardStatus" items="${applicationStatusCodes}">
                            <section class="board-column">
                                <div class="board-column__head">
                                    <span>${applicationStatusLabels[boardStatus]}</span>
                                </div>
                                <c:forEach var="x" items="${applications}">
                                    <c:if test="${x.status eq boardStatus}">
                                        <a class="board-card" href="${ctx}/apply/detail?id=${x.applicationId}">
                                            <strong>${x.companyName}</strong><span>${x.jobTitle}</span>
                                            <span>지원일 ${x.appliedDateLabel}</span>
                                            <span>다음 일정 ${empty x.nextScheduleAt ? '없음' : x.nextScheduleAtLabel}</span>
                                            <c:if test="${not empty x.nextScheduleAt}"><span class="board-card__dday">${x.scheduleDdayLabel}</span></c:if>
                                        </a>
                                    </c:if>
                                </c:forEach>
                            </section>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
