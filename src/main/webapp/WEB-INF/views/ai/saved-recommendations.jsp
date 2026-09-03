<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- [추가] AI 경험 추천에서 저장한 항목만 실제 DB에서 조회하여 보여주는 화면입니다. -->
<!doctype html>
<html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>저장한 자소서 경험 | JOBON</title>
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
                        <h1>저장한 자소서 경험</h1>
                        <p>자소서 경험 TOP3에서 저장한 프로젝트 경험을 채용공고별로 다시 확인할 수 있습니다.</p>
                    </div>
                    <a class="jobon-btn jobon-btn--soft" href="${ctx}/ai/analysis">AI 분석 목록</a>
                </section>

                <c:if test="${not empty successMessage}">
                    <div class="alert alert--success">${successMessage}</div>
                </c:if>

                <div class="recommend-list saved-recommend-list">
                    <c:forEach var="r" items="${savedRecommendations}">
                        <article class="card card--padded saved-recommend-card">
                            <div class="saved-recommend-card__head">
                                <div>
                                    <span class="badge badge--green">TOP ${r.rankNo}</span>
                                    <p class="saved-recommend-card__job">
                                        <c:choose>
                                            <c:when test="${not empty r.companyName}">${r.companyName}</c:when>
                                            <c:otherwise>기업 미연결</c:otherwise>
                                        </c:choose>
                                        · ${r.jobTitle}
                                    </p>
                                    <h3>${r.projectName}</h3>
                                </div>
                                <div class="action-row">
                                    <a class="jobon-btn jobon-btn--soft"
                                       href="${ctx}/ai/analysis/detail?id=${r.analysisId}">분석 보기</a>
                                    <c:if test="${not empty r.projectId}">
                                        <a class="jobon-btn jobon-btn--soft"
                                           href="${ctx}/project/detail?id=${r.projectId}">프로젝트 보기</a>
                                    </c:if>
                                    <form method="post" action="${ctx}/ai/recommend/${r.recommendId}/unsave"
                                          data-confirm="저장한 경험에서 해제할까요? 추천 결과 자체는 삭제되지 않습니다.">
                                        <button class="jobon-btn jobon-btn--soft" type="submit">저장 해제</button>
                                    </form>
                                </div>
                            </div>
                            <div class="saved-recommend-card__body">
                                <div>
                                    <strong>추천 이유</strong>
                                    <p>${r.reason}</p>
                                </div>
                                <div>
                                    <strong>자소서 활용 포인트</strong>
                                    <p class="muted preline">${r.sourceDetail}</p>
                                </div>
                            </div>
                        </article>
                    </c:forEach>

                    <c:if test="${empty savedRecommendations}">
                        <div class="card card--padded empty-panel">
                            저장한 자소서 경험이 없습니다. 자소서 경험 TOP3 추천 화면에서 필요한 경험을 저장해보세요.
                        </div>
                    </c:if>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
