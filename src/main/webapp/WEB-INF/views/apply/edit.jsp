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
        <title>지원 현황 등록/수정 | JOBON</title>
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
                        <h1>${empty application.applicationId ? '지원 현황 등록' : '지원 현황 수정'}</h1>
                    </div>
                </section>
                <c:set var="formAction" value="${ctx}/apply" />
                <c:if test="${not empty application.applicationId}">
                    <c:set var="formAction" value="${ctx}/apply/${application.applicationId}" />
                </c:if>
                <form class="card card--padded" method="post" action="${formAction}">
                    <div class="form-grid"><label class="form-group--full"><span class="form-label">채용공고
                                *</span><select class="form-control" name="jobId" required>
                                <option value="">선택하세요</option>
                                <c:forEach var="j" items="${jobs}">
                                    <option value="${j.jobId}" ${application.jobId eq j.jobId?'selected':''}>
                                        ${j.companyName} - ${j.title}</option>
                                </c:forEach>
                            </select></label><label><span class="form-label">지원 상태</span><select
                                class="form-control" name="status">
                                <option value="INTEREST" ${application.status eq 'INTEREST' ?'selected':''}>관심
                                </option>
                                <option value="APPLIED" ${application.status eq 'APPLIED' ?'selected':''}>지원완료
                                </option>
                                <option value="DOCUMENT" ${application.status eq 'DOCUMENT' ?'selected':''}>서류
                                </option>
                                <option value="INTERVIEW" ${application.status eq 'INTERVIEW' ?'selected':''}>면접
                                </option>
                                <option value="OFFER" ${application.status eq 'OFFER' ?'selected':''}>합격
                                </option>
                                <option value="REJECTED" ${application.status eq 'REJECTED' ?'selected':''}>불합격
                                </option>
                            </select></label><label><span class="form-label">지원일</span><input
                                class="form-control" type="date" name="appliedDate"
                                value="${application.appliedDate}"></label><label><span class="form-label">다음
                                일정</span><input class="form-control" type="datetime-local" name="nextScheduleAt"
                                value="${application.nextScheduleAtInputValue}"></label><label
                            class="form-group--full"><span class="form-label">메모</span><textarea
                                class="form-control" name="memo">${application.memo}</textarea></label></div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/apply/list">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>