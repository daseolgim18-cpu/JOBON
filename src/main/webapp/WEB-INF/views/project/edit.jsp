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
        <title>프로젝트 수정 | JOBON</title>
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
                        <h1>프로젝트 경험 수정</h1>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/project/${project.projectId}">
                    <div class="form-grid"><label class="form-group--full"><span class="form-label">프로젝트명
                                *</span><input class="form-control" name="projectName"
                                value="${project.projectName}" required></label><label><span
                                class="form-label">기업 / 소속</span><input class="form-control" name="organization"
                                value="${project.organization}"></label><label><span class="form-label">담당
                                역할</span><input class="form-control" name="roleName"
                                value="${project.roleName}"></label><label><span
                                class="form-label">시작일</span><input class="form-control" type="date"
                                name="startDate" value="${project.startDate}"></label><label><span
                                class="form-label">종료일</span><input class="form-control" type="date"
                                name="endDate" value="${project.endDate}"></label><label
                            class="form-group--full"><span class="form-label">사용 기술</span><input
                                class="form-control" name="techNames" value="${project.techNames}"
                                placeholder="Java, Spring Boot, Oracle"></label><label
                            class="form-group--full"><span class="form-label">프로젝트 내용 / 수행 경험 *</span><textarea
                                class="form-control tall" name="description"
                                required>${project.description}</textarea></label><label
                            class="form-group--full"><span class="form-label">프로젝트 URL</span><input
                                class="form-control" type="url" name="projectUrl"
                                value="${project.projectUrl}"></label></div>
                    <section class="nested-section">
                        <div class="nested-head">
                            <h3>담당 기능</h3><button type="button" class="jobon-btn jobon-btn--ghost"
                                data-add-row="featureRows">+ 기능 추가</button>
                        </div>
                        <div id="featureRows">
                            <c:forEach var="f" items="${project.features}">
                                <div class="nested-row"><input class="form-control" name="featureName"
                                        value="${f.featureName}" placeholder="기능명"><textarea
                                        class="form-control" name="featureDetail"
                                        placeholder="상세 내용">${f.detail}</textarea><button type="button"
                                        class="remove-row">삭제</button></div>
                            </c:forEach>
                            <c:if test="${empty project.features}">
                                <div class="nested-row"><input class="form-control" name="featureName"
                                        placeholder="기능명"><textarea class="form-control" name="featureDetail"
                                        placeholder="상세 내용"></textarea><button type="button"
                                        class="remove-row">삭제</button></div>
                            </c:if>
                        </div>
                    </section>
                    <section class="nested-section trouble-section">
                        <div class="nested-head">
                            <div>
                                <h3>트러블슈팅</h3>
                                <p class="nested-description">문제 상황부터 해결 결과까지 한 건씩 순서대로 기록합니다.</p>
                            </div>
                            <button type="button" class="jobon-btn jobon-btn--ghost"
                                data-add-row="troubleRows">+ 기록 추가</button>
                        </div>
                        <div id="troubleRows" class="trouble-list">
                            <c:forEach var="t" items="${project.troubles}">
                                <div class="trouble-row">
                                    <div class="trouble-row-head">
                                        <strong>트러블슈팅 기록</strong>
                                        <button type="button" class="remove-row">삭제</button>
                                    </div>
                                    <label class="trouble-field trouble-field--title">
                                        <span class="form-label">제목</span>
                                        <input class="form-control" name="troubleTitle"
                                            value="${t.title}" placeholder="트러블슈팅 제목">
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">문제</span>
                                        <textarea class="form-control trouble-textarea" name="troubleProblem"
                                            placeholder="어떤 문제가 발생했는지 구체적으로 작성하세요.">${t.problem}</textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">원인</span>
                                        <textarea class="form-control trouble-textarea" name="troubleCause"
                                            placeholder="문제가 발생한 원인을 작성하세요.">${t.cause}</textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">해결</span>
                                        <textarea class="form-control trouble-textarea" name="troubleSolution"
                                            placeholder="문제를 해결하기 위해 적용한 방법을 작성하세요.">${t.solution}</textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">결과</span>
                                        <textarea class="form-control trouble-textarea" name="troubleResult"
                                            placeholder="해결 후 달라진 점이나 개선 결과를 작성하세요.">${t.result}</textarea>
                                    </label>
                                </div>
                            </c:forEach>
                            <c:if test="${empty project.troubles}">
                                <div class="trouble-row">
                                    <div class="trouble-row-head">
                                        <strong>트러블슈팅 기록</strong>
                                        <button type="button" class="remove-row">삭제</button>
                                    </div>
                                    <label class="trouble-field trouble-field--title">
                                        <span class="form-label">제목</span>
                                        <input class="form-control" name="troubleTitle" placeholder="트러블슈팅 제목">
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">문제</span>
                                        <textarea class="form-control trouble-textarea" name="troubleProblem"
                                            placeholder="어떤 문제가 발생했는지 구체적으로 작성하세요."></textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">원인</span>
                                        <textarea class="form-control trouble-textarea" name="troubleCause"
                                            placeholder="문제가 발생한 원인을 작성하세요."></textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">해결</span>
                                        <textarea class="form-control trouble-textarea" name="troubleSolution"
                                            placeholder="문제를 해결하기 위해 적용한 방법을 작성하세요."></textarea>
                                    </label>
                                    <label class="trouble-field">
                                        <span class="form-label">결과</span>
                                        <textarea class="form-control trouble-textarea" name="troubleResult"
                                            placeholder="해결 후 달라진 점이나 개선 결과를 작성하세요."></textarea>
                                    </label>
                                </div>
                            </c:if>
                        </div>
                    </section>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/project/detail?id=${project.projectId}">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>