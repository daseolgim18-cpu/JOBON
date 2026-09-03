<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- [수정] 스토리보드/ERD 기준 실제 DB 연동 CRUD 및 화면 동작을 적용했습니다. -->
<!doctype html>
<html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>기업 상세 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="company" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container company-detail-page">
                <c:if test="${not empty successMessage}">
                    <div class="alert alert--success"><c:out value="${successMessage}" /></div>
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert--danger"><c:out value="${errorMessage}" /></div>
                </c:if>

                <!-- [추가] 현재 위치를 보여주는 기업 상세 경로 영역입니다. -->
                <nav class="company-breadcrumb" aria-label="현재 위치">
                    <a href="${ctx}/company/list">기업 관리</a>
                    <span>›</span>
                    <strong>기업 상세</strong>
                </nav>

                <div class="company-detail-layout">
                    <div class="company-detail-main">
                        <!-- [수정] COMPANY.LOGO_URL을 실제 이미지로 사용하고 DB 기업 정보를 상단 프로필 카드에 출력합니다. -->
                        <section class="card company-profile-card">
                            <div class="company-logo-box">
                                <c:choose>
                                    <c:when test="${not empty company.logoUrl}">
                                        <img class="company-logo-image"
                                             src="${company.logoUrl}"
                                             alt="${company.companyName} 로고"
                                             onerror="this.style.display='none'; this.nextElementSibling.style.display='flex';">
                                        <div class="company-logo-fallback" style="display:none;">
                                            <c:out value="${fn:substring(company.companyName, 0, 1)}" />
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="company-logo-fallback">
                                            <c:out value="${fn:substring(company.companyName, 0, 1)}" />
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="company-profile-body">
                                <div class="company-profile-head">
                                    <div>
                                        <h1><c:out value="${company.companyName}" /></h1>
                                        <div class="company-tag-row">
                                            <c:if test="${not empty company.companyType}">
                                                <span class="company-info-tag"><c:out value="${company.companyType}" /></span>
                                            </c:if>
                                            <c:if test="${not empty company.industry}">
                                                <span class="company-info-tag"><c:out value="${company.industry}" /></span>
                                            </c:if>
                                            <c:if test="${not empty company.jobField}">
                                                <span class="company-info-tag"><c:out value="${company.jobField}" /></span>
                                            </c:if>
                                        </div>
                                    </div>
                                    <div class="action-row company-profile-actions">
                                        <a class="jobon-btn jobon-btn--ghost"
                                           href="${ctx}/company/edit?id=${company.companyId}">수정</a>
                                        <form method="post" action="${ctx}/company/${company.companyId}/delete"
                                              data-confirm="기업을 삭제할까요?">
                                            <button class="jobon-btn jobon-btn--danger">삭제</button>
                                        </form>
                                    </div>
                                </div>

                                <div class="company-profile-meta">
                                    <c:if test="${not empty company.careerUrl}">
                                        <div>
                                            <span class="company-meta-label">채용 페이지</span>
                                            <a class="text-link company-meta-value" target="_blank" rel="noopener noreferrer"
                                               href="${company.careerUrl}"><c:out value="${company.careerUrl}" /></a>
                                        </div>
                                    </c:if>
                                    <c:if test="${not empty company.memo}">
                                        <div>
                                            <span class="company-meta-label">메모</span>
                                            <span class="company-meta-value company-meta-memo"><c:out value="${company.memo}" /></span>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </section>

                        <!-- [추가] 탭처럼 보이는 정보 구분 영역이며 모든 수치는 실제 DB 조회 결과를 사용합니다. -->
                        <section class="card company-information-card">
                            <div class="company-detail-tabs">
                                <a class="is-active" href="#company-info">기업 정보</a>
                                <a href="#company-jobs">채용공고 (<c:out value="${fn:length(companyJobs)}" />)</a>
                            </div>

                            <div id="company-info" class="company-info-section">
                                <h2 class="section-title">기본 정보</h2>
                                <div class="company-info-grid">
                                    <dl class="company-info-table">
                                        <div>
                                            <dt>기업 구분</dt>
                                            <dd><c:out value="${empty company.companyType ? '-' : company.companyType}" /></dd>
                                        </div>
                                        <div>
                                            <dt>산업</dt>
                                            <dd><c:out value="${empty company.industry ? '-' : company.industry}" /></dd>
                                        </div>
                                        <div>
                                            <dt>직무 분야</dt>
                                            <dd><c:out value="${empty company.jobField ? '-' : company.jobField}" /></dd>
                                        </div>
                                        <div>
                                            <dt>채용 페이지</dt>
                                            <dd>
                                                <c:choose>
                                                    <c:when test="${not empty company.careerUrl}">
                                                        <a class="text-link company-link-wrap" target="_blank" rel="noopener noreferrer"
                                                           href="${company.careerUrl}"><c:out value="${company.careerUrl}" /></a>
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </dd>
                                        </div>
                                        <div>
                                            <dt>로고 URL</dt>
                                            <dd>
                                                <c:choose>
                                                    <c:when test="${not empty company.logoUrl}">
                                                        <a class="text-link company-link-wrap" target="_blank" rel="noopener noreferrer"
                                                           href="${company.logoUrl}"><c:out value="${company.logoUrl}" /></a>
                                                    </c:when>
                                                    <c:otherwise>-</c:otherwise>
                                                </c:choose>
                                            </dd>
                                        </div>
                                        <div>
                                            <dt>등록일</dt>
                                            <dd><c:out value="${empty company.createdAtLabel ? '-' : company.createdAtLabel}" /></dd>
                                        </div>
                                        <div>
                                            <dt>수정일</dt>
                                            <dd><c:out value="${empty company.updatedAtLabel ? '-' : company.updatedAtLabel}" /></dd>
                                        </div>
                                    </dl>

                                    <aside class="company-summary-box">
                                        <h3>한눈에 보는 기업</h3>
                                        <div class="company-summary-item">
                                            <span>기업 구분</span>
                                            <strong><c:out value="${empty company.companyType ? '-' : company.companyType}" /></strong>
                                        </div>
                                        <div class="company-summary-item">
                                            <span>산업</span>
                                            <strong><c:out value="${empty company.industry ? '-' : company.industry}" /></strong>
                                        </div>
                                        <div class="company-summary-item">
                                            <span>직무 분야</span>
                                            <strong><c:out value="${empty company.jobField ? '-' : company.jobField}" /></strong>
                                        </div>
                                        <div class="company-summary-item">
                                            <span>연결 채용공고</span>
                                            <strong><c:out value="${fn:length(companyJobs)}" />건</strong>
                                        </div>
                                    </aside>
                                </div>

                                <div class="company-memo-section">
                                    <h2 class="section-title">기업 메모</h2>
                                    <div class="company-memo-box preline">
                                        <c:out value="${empty company.memo ? '등록된 메모가 없습니다.' : company.memo}" />
                                    </div>
                                </div>
                            </div>

                            <!-- [추가] JOB_POSTING.COMPANY_ID가 현재 기업 ID와 일치하는 실제 DB 공고만 출력합니다. -->
                            <div id="company-jobs" class="company-jobs-section">
                                <div class="nested-head">
                                    <h2 class="section-title">연결 채용공고</h2>
                                    <a class="jobon-btn jobon-btn--ghost" href="${ctx}/job/list">전체 채용공고</a>
                                </div>
                                <c:choose>
                                    <c:when test="${empty companyJobs}">
                                        <div class="company-empty-state">이 기업에 연결된 채용공고가 없습니다.</div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="company-job-list">
                                            <c:forEach var="job" items="${companyJobs}">
                                                <a class="company-job-row" href="${ctx}/job/detail?id=${job.jobId}">
                                                    <div>
                                                        <strong><c:out value="${job.title}" /></strong>
                                                        <p>
                                                            <c:out value="${job.jobRole}" />
                                                            <c:if test="${not empty job.employmentType}"> · <c:out value="${job.employmentType}" /></c:if>
                                                            <c:if test="${not empty job.region}"> · <c:out value="${job.region}" /></c:if>
                                                        </p>
                                                    </div>
                                                    <span class="company-job-deadline"><c:out value="${job.deadlineDdayLabel}" /></span>
                                                </a>
                                            </c:forEach>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </section>
                    </div>

                    <aside class="company-detail-sidebar">
                        <section class="card company-side-card">
                            <h2>바로가기</h2>
                            <a href="${ctx}/company/edit?id=${company.companyId}">기업 정보 수정 <span>›</span></a>
                            <a href="${ctx}/job/list">채용공고 목록 <span>›</span></a>
                            <a href="${ctx}/company/list">기업 목록 <span>›</span></a>
                        </section>

                        <!-- [추가] 최근 채용공고 역시 화면용 임의 값이 아니라 COMPANY_ID로 조회된 DB 데이터만 사용합니다. -->
                        <section class="card company-side-card">
                            <div class="company-side-title-row">
                                <h2>최근 연결 채용공고</h2>
                                <a href="#company-jobs">더보기</a>
                            </div>
                            <c:choose>
                                <c:when test="${empty companyJobs}">
                                    <p class="company-side-empty">연결된 채용공고가 없습니다.</p>
                                </c:when>
                                <c:otherwise>
                                    <div class="company-recent-job-list">
                                        <c:forEach var="job" items="${companyJobs}" varStatus="status">
                                            <c:if test="${status.index lt 3}">
                                                <a href="${ctx}/job/detail?id=${job.jobId}">
                                                    <strong><c:out value="${job.title}" /></strong>
                                                    <span><c:out value="${job.deadlineDdayLabel}" /></span>
                                                </a>
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </section>
                    </aside>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
