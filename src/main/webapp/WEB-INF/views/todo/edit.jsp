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
        <title>TODO 수정 | JOBON</title>
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
                        <h1>TODO 수정</h1>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/todo/${todo.todoId}">
                    <div class="form-grid"><label class="form-group--full"><span class="form-label">할 일
                                *</span><input class="form-control" name="title" value="${todo.title}"
                                required></label><label><span class="form-label">우선순위</span><select
                                class="form-control" name="priority">
                                <option value="HIGH" ${todo.priority eq 'HIGH' ?'selected':''}>높음</option>
                                <option value="MEDIUM" ${todo.priority eq 'MEDIUM' ?'selected':''}>보통</option>
                                <option value="LOW" ${todo.priority eq 'LOW' ?'selected':''}>낮음</option>
                            </select></label><label><span class="form-label">마감일</span><input
                                class="form-control" type="date" name="dueDate"
                                value="${todo.dueDate}"></label><label><span class="form-label">관련
                                기업</span><select class="form-control" name="companyId">
                                <option value="">없음</option>
                                <c:forEach var="c" items="${companies}">
                                    <option value="${c.companyId}" ${todo.companyId eq
                                        c.companyId?'selected':''}>${c.companyName}</option>
                                </c:forEach>
                            </select></label><label><span class="form-label">관련 채용공고</span><select
                                class="form-control" name="jobId">
                                <option value="">없음</option>
                                <c:forEach var="j" items="${jobs}">
                                    <option value="${j.jobId}" ${todo.jobId eq j.jobId?'selected':''}>${j.title}
                                    </option>
                                </c:forEach>
                            </select></label><label><span class="form-label">상태</span><select
                                class="form-control" name="status">
                                <option value="TODO" ${todo.status eq 'TODO' ?'selected':''}>할 일</option>
                                <option value="DOING" ${todo.status eq 'DOING' ?'selected':''}>진행 중</option>
                                <option value="DONE" ${todo.status eq 'DONE' ?'selected':''}>완료</option>
                            </select></label><label class="form-group--full"><span
                                class="form-label">메모</span><textarea class="form-control"
                                name="memo">${todo.memo}</textarea></label></div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/todo/list">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>