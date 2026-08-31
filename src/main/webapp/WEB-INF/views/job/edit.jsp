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
        <title>채용공고 수정 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="job" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>채용공고 수정</h1>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/job/${job.jobId}">
                    <div class="form-grid"><label><span class="form-label">기업</span><select class="form-control"
                                name="companyId">
                                <option value="">직접/미지정</option>
                                <c:forEach var="c" items="${companies}">
                                    <option value="${c.companyId}" ${job.companyId eq
                                        c.companyId?'selected':''}>${c.companyName}</option>
                                </c:forEach>
                            </select></label><label><span class="form-label">채용 직무 *</span><input
                                class="form-control" name="jobRole" value="${job.jobRole}"
                                required></label><label class="form-group--full"><span class="form-label">공고명
                                *</span><input class="form-control" name="title" value="${job.title}"
                                required></label><label><span class="form-label">등록일</span><input
                                class="form-control" type="date" name="postedDate"
                                value="${job.postedDate}"></label><label><span
                                class="form-label">마감일</span><input class="form-control" type="date"
                                name="deadline" value="${job.deadline}"></label><label><span
                                class="form-label">고용형태</span><input class="form-control" name="employmentType"
                                value="${job.employmentType}" placeholder="신입/경력/인턴"></label><label><span
                                class="form-label">지역</span><input class="form-control" name="region"
                                value="${job.region}"></label><label class="form-group--full"><span
                                class="form-label">출처 URL</span><input class="form-control" type="url"
                                name="sourceUrl" value="${job.sourceUrl}"></label><label
                            class="form-group--full"><span class="form-label">채용공고 원문 *</span><textarea
                                class="form-control tall" name="originalText"
                                required>${job.originalText}</textarea></label><label
                            class="form-group--full"><span class="form-label">메모</span><textarea
                                class="form-control" name="memo">${job.memo}</textarea></label></div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/job/detail?id=${job.jobId}">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>