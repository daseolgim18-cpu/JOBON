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
        <title>TODO | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="todo" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>TODO</h1>
                        <p>채용공고 마감일과 연결해 취업 준비 할 일을 관리하세요.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/todo/new">TODO 등록</a>
                </section>
                <div class="chip-row"><a class="chip" href="${ctx}/todo/list">전체</a><a class="chip"
                        href="${ctx}/todo/list?status=TODO">할 일</a><a class="chip"
                        href="${ctx}/todo/list?status=DOING">진행중</a><a class="chip"
                        href="${ctx}/todo/list?status=DONE">완료</a></div>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>상태</th>
                                <th>할 일</th>
                                <th>우선순위</th>
                                <th>마감일</th>
                                <%-- [수정] 마감일과 D-Day를 별도 열로 분리합니다. --%>
                                <th class="dday-column">D-Day</th>
                                <th>연결</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${todos}">
                                <tr>
                                    <td class="todo-status-cell">
                                        <!-- [수정] 수정 화면으로 이동하지 않고 TODO 상태를 목록에서 바로 변경합니다. -->
                                        <form class="todo-status-form" method="post"
                                              action="${ctx}/todo/${x.todoId}/status">
                                            <c:if test="${status eq 'TODO' or status eq 'DOING' or status eq 'DONE'}">
                                                <input type="hidden" name="filter" value="${status}">
                                            </c:if>
                                            <select class="todo-status-select ${x.status eq 'DONE' ? 'todo-status-select--done' : (x.status eq 'DOING' ? 'todo-status-select--doing' : 'todo-status-select--todo')}"
                                                    name="status" aria-label="TODO 상태 변경"
                                                    onchange="this.form.submit()">
                                                <option value="TODO" ${x.status eq 'TODO' ? 'selected' : ''}>할 일</option>
                                                <option value="DOING" ${x.status eq 'DOING' ? 'selected' : ''}>진행 중</option>
                                                <option value="DONE" ${x.status eq 'DONE' ? 'selected' : ''}>완료</option>
                                            </select>
                                        </form>
                                    </td>
                                    <td class="${x.status eq 'DONE'?'strike':''}">${x.title}</td>
                                    <td>${x.priorityLabel}</td>
                                    <td class="date-cell">${empty x.dueDate ? '-' : x.dueDateLabel}</td>
                                    <td class="dday-cell"><c:if test="${not empty x.dueDate}"><span class="deadline-badge">${x.dueDdayLabel}</span></c:if><c:if test="${empty x.dueDate}">-</c:if></td>
                                    <td class="relation-cell"><span>${x.companyName}</span><span>${x.jobTitle}</span></td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/todo/edit?id=${x.todoId}">수정</a>
                                            <form method="post" action="${ctx}/todo/${x.todoId}/delete"
                                                data-confirm="TODO를 삭제할까요?"><button
                                                    class="text-link danger">삭제</button></form>
                                        </div>
                                    </td>
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
