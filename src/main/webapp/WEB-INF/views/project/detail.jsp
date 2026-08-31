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
        <title>프로젝트 상세 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="project" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>${project.projectName}</h1>
                        <p>${project.organization} · ${project.roleName}</p>
                    </div>
                    <div class="action-row"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/project/edit?id=${project.projectId}">수정</a>
                        <form method="post" action="${ctx}/project/${project.projectId}/delete"
                            data-confirm="프로젝트를 삭제할까요?"><button class="jobon-btn jobon-btn--danger">삭제</button>
                        </form>
                    </div>
                </section>
                <div class="card card--padded">
                    <div class="tag-cloud">
                        <c:forTokens items="${project.techNames}" delims="," var="t"><span
                                class="skill-tag">${t}</span></c:forTokens>
                    </div>
                    <h3>프로젝트 경험</h3>
                    <p class="preline">${project.description}</p>
                    <h3>담당 기능</h3>
                    <c:forEach var="f" items="${project.features}">
                        <div class="story-card"><strong>${f.featureName}</strong>
                            <p class="preline">${f.detail}</p>
                        </div>
                    </c:forEach>
                    <h3>트러블슈팅</h3>
                    <c:forEach var="t" items="${project.troubles}">
                        <div class="story-card"><strong>${t.title}</strong>
                            <dl class="mini-detail">
                                <dt>문제</dt>
                                <dd>${t.problem}</dd>
                                <dt>원인</dt>
                                <dd>${t.cause}</dd>
                                <dt>해결</dt>
                                <dd>${t.solution}</dd>
                                <dt>결과</dt>
                                <dd>${t.result}</dd>
                            </dl>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>