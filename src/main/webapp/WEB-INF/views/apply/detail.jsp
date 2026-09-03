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
        <title>지원 현황 상세 | JOBON</title>
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
                    <h1>${application.companyName}</h1>
                    <p>${application.jobTitle}</p>
                </div>
                <div class="action-row"><a class="jobon-btn jobon-btn--ghost"
                        href="${ctx}/apply/edit?id=${application.applicationId}">수정</a>
                    <form method="post" action="${ctx}/apply/${application.applicationId}/delete"
                        data-confirm="지원 현황을 삭제할까요?"><button class="jobon-btn jobon-btn--danger">삭제</button>
                    </form>
                </div>
            </section>
            <div class="card card--padded">
                <dl class="detail-list">
                    <div>
                        <dt>상태</dt>
                        <dd>${application.statusLabel}</dd>
                    </div>
                    <div>
                        <dt>지원일</dt>
                        <dd>${application.appliedDateLabel}</dd>
                    </div>
                    <div>
                        <dt>다음 일정</dt>
                        <dd>${empty application.nextScheduleAt ? '없음' : application.nextScheduleAtLabel} <c:if test="${not empty application.nextScheduleAt}"><span class="badge">${application.scheduleDdayLabel}</span></c:if></dd>
                    </div>
                    <div>
                        <dt>메모</dt>
                        <dd class="preline">${application.memo}</dd>
                    </div>
                </dl>
            </div>
            <!-- [추가] ACTIVITY_LOG를 이용한 지원 진행 변경 이력 -->
            <section class="card card--padded mt20">
                <h3>지원 진행 이력</h3>
                <c:forEach var="h" items="${applicationHistory}">
                    <div class="dashboard-row dashboard-row--activity">
                        <span>${h.title}</span><span class="muted">${h.formattedCreatedAt}</span>
                    </div>
                </c:forEach>
                <c:if test="${empty applicationHistory}"><p class="muted">아직 기록된 변경 이력이 없습니다.</p></c:if>
            </section>
            <div class="quick-actions"><a class="jobon-btn jobon-btn--ghost"
                    href="${ctx}/job/detail?id=${application.jobId}">채용공고 보기</a><a
                    class="jobon-btn jobon-btn--ghost"
                    href="${ctx}/todo/new?jobId=${application.jobId}">TODO 생성</a><a
                    class="jobon-btn jobon-btn--primary"
                    href="${ctx}/ai/job-analysis?jobId=${application.jobId}">AI 분석 결과 보기</a></div>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>