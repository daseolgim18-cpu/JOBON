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
        <title>기업 등록 | JOBON</title>
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
                        <h1>기업 등록</h1>
                        <p>관심 기업의 기본 정보를 저장합니다.</p>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/company">
                    <!-- [수정] 상세 화면에 표시할 기업 정보는 입력 폼부터 COMPANY 컬럼과 1:1로 저장합니다. -->
                    <div class="form-grid">
                        <label><span class="form-label">기업명 *</span><input class="form-control"
                                name="companyName" maxlength="150" value="${company.companyName}" required></label>
                        <label><span class="form-label">기업 구분</span><input class="form-control"
                                name="companyType" maxlength="50" value="${company.companyType}"
                                placeholder="예: 중소기업"></label>
                        <label><span class="form-label">산업</span><input class="form-control"
                                name="industry" maxlength="100" value="${company.industry}"
                                placeholder="예: IT"></label>
                        <label><span class="form-label">직무 분야</span><input class="form-control"
                                name="jobField" maxlength="100" value="${company.jobField}"
                                placeholder="예: 웹개발"></label>
                        <label class="form-group--full"><span class="form-label">기업 업종</span><input
                                class="form-control" name="businessType" maxlength="150"
                                value="${company.businessType}" placeholder="예: 솔루션·SI·ERP·CRM"></label>
                        <label class="form-group--full"><span class="form-label">홈페이지</span><input
                                class="form-control" type="url" name="homepageUrl" maxlength="500"
                                value="${company.homepageUrl}" placeholder="https://example.com"></label>
                        <label class="form-group--full"><span class="form-label">사업내용</span><textarea
                                class="form-control" name="businessDescription"
                                placeholder="기업의 주요 사업내용을 입력하세요.">${company.businessDescription}</textarea></label>
                        <label class="form-group--full"><span class="form-label">주소</span><input
                                class="form-control" name="address" maxlength="500" value="${company.address}"
                                placeholder="기업 주소를 입력하세요."></label>
                        <label class="form-group--full"><span class="form-label">채용 페이지</span><input
                                class="form-control" type="url" name="careerUrl" maxlength="500"
                                value="${company.careerUrl}" placeholder="https://example.com/recruit"></label>
                        <label class="form-group--full"><span class="form-label">로고 URL</span><input
                                class="form-control" type="url" name="logoUrl" maxlength="500"
                                value="${company.logoUrl}" placeholder="https://example.com/logo.png"></label>
                        <label class="form-group--full"><span class="form-label">메모</span><textarea
                                class="form-control" name="memo"
                                placeholder="관심 포인트나 참고사항을 자유롭게 입력하세요.">${company.memo}</textarea></label>
                    </div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/company/list">취소</a><button
                            class="jobon-btn jobon-btn--primary">등록</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>
