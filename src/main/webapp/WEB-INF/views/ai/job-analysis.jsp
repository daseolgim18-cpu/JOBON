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
        <title>AI 채용공고 분석 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
    <c:set var="activeMenu" value="ai" scope="request" />
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
        <div class="jobon-container">
            <section class="page-heading">
                <div>
                    <h1>AI 채용공고 분석</h1>
                    <p>${analysis.companyName} · ${analysis.jobTitle}</p>
                </div>
                <div class="action-row">
                    <form method="post" action="${ctx}/ai/analysis/${analysis.analysisId}/rerun"><button
                            class="jobon-btn jobon-btn--ghost">다시 분석</button></form><a
                        class="jobon-btn jobon-btn--primary"
                        href="${ctx}/ai/experience-recommend?analysisId=${analysis.analysisId}">경험 TOP3</a>
                </div>
            </section>
            <!-- [추가] 분석 자체를 막지 않고 입력 데이터 품질을 안내합니다. -->
            <c:if test="${not empty dataQualityWarnings}">
                <div class="alert">
                    <strong>분석 데이터 보완 안내</strong>
                    <ul><c:forEach var="w" items="${dataQualityWarnings}"><li>${w}</li></c:forEach></ul>
                </div>
            </c:if>
            <c:if test="${analysis.status eq 'FAILED'}">
                <div class="alert alert--danger">
                    <strong>AI 분석을 완료하지 못했습니다.</strong><br>${analysis.errorMessage}
                    <p>공고 원문과 LLM 설정을 확인한 뒤 위의 다시 분석 버튼을 눌러주세요.</p>
                </div>
            </c:if>
            <c:if test="${analysis.status eq 'COMPLETED'}">
            <c:if test="${analysis.modelName eq 'local-fallback'}">
                <div class="alert">외부 LLM 대신 로컬 분석 결과를 사용했습니다. ${analysis.errorMessage}</div>
            </c:if>
            <div class="readiness">
                <c:choose>
                    <c:when test="${empty analysis.readinessScore}">
                        <strong>-</strong><span>지원 준비도 · 명시 기술 없음</span>
                    </c:when>
                    <c:otherwise>
                        <strong>${analysis.readinessScore}%</strong><span>지원 준비도</span>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="analysis-grid">
                <article class="card card--padded">
                    <h3>요약</h3>
                    <p class="preline">${analysis.summary}</p>
                </article>
                <article class="card card--padded">
                    <h3>주요 업무</h3>
                    <p class="preline">${analysis.mainTasks}</p>
                </article>
                <article class="card card--padded">
                    <h3>자격요건</h3>
                    <p class="preline">${analysis.qualifications}</p>
                </article>
                <article class="card card--padded">
                    <h3>우대사항</h3>
                    <p class="preline">${analysis.preferences}</p>
                </article>
            </div>
            <section class="card card--padded mt20">
                <h3>필수·우대 기술 / 보유 역량 비교</h3>
                <c:choose>
                    <c:when test="${empty analysis.techs}">
                        <div class="empty-panel">
                            채용공고 원문에서 Java, Spring Boot, Oracle 같은 구체적인 기술 스택이 명시되지 않아
                            기술 보유 여부와 지원 준비도는 계산하지 않았습니다.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="skill-matrix">
                            <c:forEach var="t" items="${analysis.techs}">
                                <div class="skill-row">
                                    <span>${t.techName}</span>
                                    <span class="badge">
                                        <c:choose>
                                            <c:when test="${t.requirementType eq 'REQUIRED'}">필수</c:when>
                                            <c:when test="${t.requirementType eq 'PREFERRED'}">우대</c:when>
                                            <c:otherwise>${t.requirementType}</c:otherwise>
                                        </c:choose>
                                    </span>
                                    <span class="match match--${t.matchStatus}">
                                        ${t.matchStatus eq 'OWNED'?'보유':t.matchStatus eq 'PARTIAL'?'부분일치':'부족'}
                                    </span>
                                </div>
                            </c:forEach>
                        </div>
                        <h4>부분일치·부족 기술별 학습 방향</h4>
                        <ul>
                            <c:forEach var="t" items="${analysis.techs}">
                                <c:if test="${t.matchStatus ne 'OWNED'}">
                                    <li><strong>${t.techName}</strong> — ${t.learningDirection}
                                        <a class="text-link" href="${ctx}/learning/new?tech=${t.techName}">학습 기록 추가</a>
                                    </li>
                                </c:if>
                            </c:forEach>
                        </ul>
                    </c:otherwise>
                </c:choose>
            </section>
            <section class="card card--padded mt20">
                <h3>예상 면접 질문</h3>
                <ol>
                    <c:forEach var="q" items="${analysis.interviewQuestions}">
                        <li>${q}</li>
                    </c:forEach>
                </ol>
            </section>
            </c:if>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
