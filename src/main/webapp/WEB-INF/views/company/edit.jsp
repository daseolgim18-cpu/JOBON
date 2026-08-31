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
        <title>기업 수정 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="company" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>기업 수정</h1>
                        <p>등록한 기업 정보를 변경합니다.</p>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/company/${company.companyId}">
                    <div class="form-grid"><label><span class="form-label">기업명 *</span><input
                                class="form-control" name="companyName" value="${company.companyName}"
                                required></label><label><span class="form-label">기업 구분</span><input
                                class="form-control" name="companyType" value="${company.companyType}"
                                placeholder="대기업/스타트업 등"></label><label><span class="form-label">산업</span><input
                                class="form-control" name="industry"
                                value="${company.industry}"></label><label><span class="form-label">직무
                                분야</span><input class="form-control" name="jobField"
                                value="${company.jobField}"></label><label class="form-group--full"><span
                                class="form-label">채용 페이지 URL</span><input class="form-control" type="url"
                                name="careerUrl" value="${company.careerUrl}"></label><label
                            class="form-group--full"><span class="form-label">로고 URL</span><input
                                class="form-control" type="url" name="logoUrl"
                                value="${company.logoUrl}"></label><label class="form-group--full"><span
                                class="form-label">메모</span><textarea class="form-control"
                                name="memo">${company.memo}</textarea></label></div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/company/detail?id=${company.companyId}">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>