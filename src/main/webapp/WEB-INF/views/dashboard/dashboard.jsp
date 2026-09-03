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
        <title>대시보드 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <!-- [수정] 대시보드 UI 수정 후 브라우저의 이전 CSS 캐시가 남지 않도록 버전 쿼리를 추가했습니다. -->
        <link rel="stylesheet" href="${ctx}/css/domain.css?v=20260902-2">
    </head>

    <body>
        <c:set var="activeMenu" value="dashboard" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>대시보드</h1>
                        <p>취업 준비 흐름을 한눈에 확인하세요.</p>
                    </div>
                </section>
                <div class="metric-grid">
                    <a class="metric card" href="${ctx}/company/list"><strong>${companies.size()}</strong><span>기업</span></a>
                    <a class="metric card" href="${ctx}/job/list"><strong>${jobs.size()}</strong><span>채용공고</span></a>
                    <a class="metric card" href="${ctx}/apply/list"><strong>${applications.size()}</strong><span>지원현황</span></a>
                    <a class="metric card" href="${ctx}/todo/list"><strong>${todos.size()}</strong><span>TODO</span></a>
                    <a class="metric card" href="${ctx}/ai/analysis"><strong>${analyses.size()}</strong><span>AI 분석</span></a>
                </div>

                <!-- [추가] 지원 단계별 현황을 실제 APPLICATION 상태값으로 집계합니다. -->
                <section class="card card--padded mt20">
                    <div class="nested-head"><h3>지원 단계 요약</h3><a class="text-link" href="${ctx}/apply/list?view=board">보드 보기</a></div>
                    <div class="status-summary">
                        <a href="${ctx}/apply/list?status=INTEREST"><strong>${applicationStatusCounts.INTEREST}</strong><span>관심</span></a>
                        <a href="${ctx}/apply/list?status=APPLIED"><strong>${applicationStatusCounts.APPLIED}</strong><span>지원완료</span></a>
                        <a href="${ctx}/apply/list?status=DOCUMENT"><strong>${applicationStatusCounts.DOCUMENT}</strong><span>서류</span></a>
                        <a href="${ctx}/apply/list?status=CODING_TEST"><strong>${applicationStatusCounts.CODING_TEST}</strong><span>코딩테스트</span></a>
                        <a href="${ctx}/apply/list?status=INTERVIEW"><strong>${applicationStatusCounts.INTERVIEW}</strong><span>면접</span></a>
                        <a href="${ctx}/apply/list?status=OFFER"><strong>${applicationStatusCounts.OFFER}</strong><span>합격</span></a>
                        <a href="${ctx}/apply/list?status=REJECTED"><strong>${applicationStatusCounts.REJECTED}</strong><span>불합격</span></a>
                    </div>
                </section>

                <div class="analysis-grid mt20">
                    <section class="card card--padded">
                        <div class="nested-head"><h3>7일 이내 마감 공고</h3><a class="text-link" href="${ctx}/job/list?sort=deadline">전체 보기</a></div>
                        <c:forEach var="j" items="${imminentJobs}">
                            <div class="dashboard-row"><a href="${ctx}/job/detail?id=${j.jobId}">${j.companyName} · ${j.title}</a>
                                <span class="deadline-badge">${j.deadlineDdayLabel}</span></div>
                        </c:forEach>
                        <c:if test="${empty imminentJobs}"><p class="muted">7일 이내 마감되는 공고가 없습니다.</p></c:if>
                    </section>
                    <section class="card card--padded">
                        <div class="nested-head"><h3>다가오는 지원 일정</h3><a class="text-link" href="${ctx}/apply/list?sort=schedule">전체 보기</a></div>
                        <c:forEach var="x" items="${upcomingSchedules}">
                            <div class="dashboard-row dashboard-row--schedule">
                                <a class="dashboard-row__title" href="${ctx}/apply/detail?id=${x.applicationId}">${x.companyName} · ${x.jobTitle}</a>
                                <%-- [수정] 일정 시각과 D-Day를 각각 독립 영역으로 분리해 겹침을 방지합니다. --%>
                                <span class="dashboard-row__meta dashboard-row__date">${x.nextScheduleAtLabel}</span>
                                <span class="deadline-badge dashboard-row__dday">${x.scheduleDdayLabel}</span>
                            </div>
                        </c:forEach>
                        <c:if test="${empty upcomingSchedules}"><p class="muted">등록된 다음 일정이 없습니다.</p></c:if>
                    </section>
                    <section class="card card--padded">
                        <div class="nested-head"><h3>진행할 TODO</h3><a class="text-link" href="${ctx}/todo/list?status=TODO">전체 보기</a></div>
                        <c:forEach var="t" items="${pendingTodos}">
                            <div class="dashboard-row dashboard-row--todo">
                                <a class="dashboard-row__title" href="${ctx}/todo/edit?id=${t.todoId}">${t.title}</a>
                                <%-- [수정] 날짜/D-Day/우선순위/액션을 각각 분리해 좁은 카드에서도 글자가 겹치지 않게 합니다. --%>
                                <span class="dashboard-row__meta dashboard-row__date">${t.dueDateLabel}</span>
                                <span class="deadline-badge dashboard-row__dday"><c:if test="${not empty t.dueDate}">${t.dueDdayLabel}</c:if></span>
                                <span class="dashboard-row__priority">${t.priorityLabel}</span>
                                <form class="dashboard-row__action" method="post" action="${ctx}/todo/${t.todoId}/complete">
                                    <button class="todo-complete-btn" type="submit">✓ 완료 처리</button>
                                </form>
                            </div>
                        </c:forEach>
                        <c:if test="${empty pendingTodos}"><p class="muted">진행할 TODO가 없습니다.</p></c:if>
                    </section>
                    <section class="card card--padded">
                        <div class="nested-head"><h3>최근 활동</h3><a class="text-link" href="${ctx}/mypage/activity">전체 보기</a></div>
                        <c:forEach var="x" items="${recentActivities}">
                            <div class="dashboard-row dashboard-row--activity">
                                <span class="dashboard-activity__content">
                                    <span class="badge dashboard-activity__badge">${x.activityTypeLabel}</span>
                                    <span class="dashboard-activity__title">${x.title}</span>
                                </span>
                                <span class="dashboard-row__meta" title="${x.formattedCreatedAt}">${x.relativeTime}</span>
                            </div>
                        </c:forEach>
                        <c:if test="${empty recentActivities}"><p class="muted">아직 기록된 활동이 없습니다.</p></c:if>
                    </section>
                </div>

                <div class="analysis-grid mt20">
                    <section class="card card--padded">
                        <div class="nested-head"><h3>최근 프로젝트 경험</h3><a class="text-link" href="${ctx}/project/list">전체 보기</a></div>
                        <c:forEach var="p" items="${projectRecords}">
                            <div class="dashboard-record"><a href="${ctx}/project/detail?id=${p.projectId}"><strong>${p.projectName}</strong></a>
                                <p class="muted">${p.roleName} · ${p.techNames}</p></div>
                        </c:forEach>
                        <c:if test="${empty projectRecords}"><p class="muted">등록된 프로젝트 경험이 없습니다.</p></c:if>
                    </section>
                    <section class="card card--padded">
                        <div class="nested-head"><h3>최근 학습 기록</h3><a class="text-link" href="${ctx}/learning/list">전체 보기</a></div>
                        <c:forEach var="l" items="${learningRecords}">
                            <div class="dashboard-record"><a href="${ctx}/learning/detail?id=${l.learningId}"><strong>${l.subject}</strong></a>
                                <p class="muted">${l.learningDate} · ${l.techNames}</p></div>
                        </c:forEach>
                        <c:if test="${empty learningRecords}"><p class="muted">등록된 학습 기록이 없습니다.</p></c:if>
                    </section>
                </div>

                <!-- [추가] 최근 완료된 AI 분석의 준비도와 부족 역량을 대시보드에 표시합니다. -->
                <section class="card card--padded mt20">
                    <div class="nested-head"><h3>최근 AI 준비도·부족 역량</h3><a class="text-link" href="${ctx}/ai/analysis">AI 분석</a></div>
                    <c:choose>
                        <c:when test="${not empty latestAnalysis}">
                            <div class="dashboard-ai">
                                <div class="readiness"><strong>${latestAnalysis.readinessScore}%</strong><span>${latestAnalysis.companyName} · ${latestAnalysis.jobTitle}</span></div>
                                <div class="tag-cloud">
                                    <c:forEach var="tech" items="${latestAnalysis.techs}">
                                        <c:if test="${tech.matchStatus eq 'MISSING'}"><span class="skill-tag skill-tag--missing">부족 · ${tech.techName}</span></c:if>
                                    </c:forEach>
                                </div>
                                <a class="jobon-btn jobon-btn--ghost" href="${ctx}/ai/analysis/detail?id=${latestAnalysis.analysisId}">상세 분석 보기</a>
                            </div>
                        </c:when>
                        <c:otherwise><p class="muted">완료된 AI 분석이 없습니다. 채용공고를 등록한 뒤 분석을 실행해주세요.</p></c:otherwise>
                    </c:choose>
                </section>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
