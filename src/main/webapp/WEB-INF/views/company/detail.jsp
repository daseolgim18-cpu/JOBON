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
        <title>기업 상세 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="company" scope="request" />
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
                        <h1>${company.companyName}</h1>
                        <p>기업 상세 정보</p>
                    </div>
                    <div class="action-row"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/company/edit?id=${company.companyId}">수정</a>
                        <form method="post" action="${ctx}/company/${company.companyId}/delete"
                            data-confirm="기업을 삭제할까요?"><button class="jobon-btn jobon-btn--danger">삭제</button>
                        </form>
                    </div>
                </section>
                <div class="card card--padded">
                    <dl class="detail-list">
                        <div>
                            <dt>기업 구분</dt>
                            <dd>${company.companyType}</dd>
                        </div>
                        <div>
                            <dt>산업 / 직무</dt>
                            <dd>${company.industry} · ${company.jobField}</dd>
                        </div>
                        <div>
                            <dt>채용 페이지</dt>
                            <dd><a class="text-link" target="_blank"
                                    href="${company.careerUrl}">${company.careerUrl}</a></dd>
                        </div>
                        <div>
                            <dt>메모</dt>
                            <dd class="preline">${company.memo}</dd>
                        </div>
                    </dl>
                </div>
                <div class="form-actions"><a class="jobon-btn jobon-btn--primary"
                        href="${ctx}/job/list?keyword=${company.companyName}">연결 채용공고 보기</a></div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>