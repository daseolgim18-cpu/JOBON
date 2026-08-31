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
            <div class="readiness"><strong>${analysis.readinessScore}%</strong><span>지원 준비도</span></div>
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
                <div class="skill-matrix">
                    <c:forEach var="t" items="${analysis.techs}">
                        <div class="skill-row"><span>${t.techName}</span><span
                                class="badge">${t.requirementType}</span><span
                                class="match match--${t.matchStatus}">${t.matchStatus eq
                                'OWNED'?'보유':t.matchStatus eq 'PARTIAL'?'부분일치':'부족'}</span></div>
                    </c:forEach>
                </div>
                <h4>부족 기술 학습 방향</h4>
                <ul>
                    <c:forEach var="t" items="${analysis.techs}">
                        <c:if test="${t.matchStatus eq 'MISSING'}">
                            <li><strong>${t.techName}</strong> — 공식 문서/기초 실습 → 작은 프로젝트 적용 → 성장 기록에 학습 근거 저장
                            </li>
                        </c:if>
                    </c:forEach>
                </ul>
            </section>
            <section class="card card--padded mt20">
                <h3>예상 면접 질문</h3>
                <ol>
                    <c:forEach var="q" items="${analysis.interviewQuestions}">
                        <li>${q}</li>
                    </c:forEach>
                </ol>
            </section>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>