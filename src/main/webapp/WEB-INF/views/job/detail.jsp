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
        <title>채용공고 상세 | JOBON</title>
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
                        <h1>${job.title}</h1>
                        <p>${job.companyName} · ${job.jobRole}</p>
                    </div>
                    <div class="action-row"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/job/edit?id=${job.jobId}">수정</a>
                        <form method="post" action="${ctx}/job/${job.jobId}/delete" data-confirm="채용공고를 삭제할까요?">
                            <button class="jobon-btn jobon-btn--danger">삭제</button></form>
                    </div>
                </section>
                <div class="card card--padded">
                    <dl class="detail-list">
                        <div>
                            <dt>마감일</dt>
                            <dd>${job.deadline} <c:if test="${not empty job.deadline}"><span class="deadline-badge">${job.deadlineDdayLabel}</span></c:if></dd>
                        </div>
                        <div>
                            <dt>고용형태/지역</dt>
                            <dd>${job.employmentType} · ${job.region}</dd>
                        </div>
                        <div>
                            <dt>출처</dt>
                            <dd><a target="_blank" class="text-link"
                                    href="${job.sourceUrl}">${job.sourceUrl}</a></dd>
                        </div>
                        <div>
                            <dt>공고 원문</dt>
                            <dd class="preline">${job.originalText}</dd>
                        </div>
                        <div>
                            <dt>메모</dt>
                            <dd class="preline">${job.memo}</dd>
                        </div>
                    </dl>
                </div>
                <c:if test="${not empty currentApplication}">
                    <div class="card card--padded mt20">
                        <div class="nested-head"><h3>현재 지원 상태</h3><span class="badge badge--green">${currentApplication.statusLabel}</span></div>
                        <p class="muted">지원일 ${empty currentApplication.appliedDateLabel ? '-' : currentApplication.appliedDateLabel} · 다음 일정 ${empty currentApplication.nextScheduleAtLabel ? '없음' : currentApplication.nextScheduleAtLabel}</p>
                        <a class="text-link" href="${ctx}/apply/detail?id=${currentApplication.applicationId}">지원현황 상세 보기</a>
                    </div>
                </c:if>
                <div class="quick-actions">
                    <c:choose><c:when test="${empty currentApplication}"><a class="jobon-btn jobon-btn--primary" href="${ctx}/apply/new?jobId=${job.jobId}">지원현황 등록</a></c:when><c:otherwise><a class="jobon-btn jobon-btn--primary" href="${ctx}/apply/detail?id=${currentApplication.applicationId}">지원현황 보기</a></c:otherwise></c:choose>
                    <a class="jobon-btn jobon-btn--ghost" href="${ctx}/todo/new?jobId=${job.jobId}">마감일 TODO 생성</a>
                    <form method="post" action="${ctx}/ai/analysis"><input type="hidden" name="jobId"
                            value="${job.jobId}"><button class="jobon-btn jobon-btn--soft">AI 분석 시작</button>
                    </form>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>