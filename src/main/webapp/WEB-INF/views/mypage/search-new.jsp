<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>새 검색어 저장 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
        <div class="jobon-container">
            <section class="page-heading"><div><h1>새 검색어 저장</h1><p>기업·채용공고 통합 검색에 다시 사용할 조건을 저장하세요.</p></div></section>
            <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
            <form class="card card--padded" method="post" action="${ctx}/mypage/searches">
                <div class="form-grid">
                    <label class="form-group form-group--full"><span class="form-label">검색어 이름 *</span><input class="form-control" name="searchName" type="text" maxlength="100" required placeholder="예: 백엔드 신입 서울" /></label>
                    <label class="form-group"><span class="form-label">검색 대상</span><select class="form-control" name="targetType"><option value="ALL">전체</option><option value="COMPANY">기업</option><option value="JOB">채용공고</option></select></label>
                    <label class="form-group"><span class="form-label">키워드</span><input class="form-control" name="keyword" type="text" maxlength="200" placeholder="기업명, 공고명, 키워드" /></label>
                    <label class="form-group"><span class="form-label">직무</span><input class="form-control" name="jobRole" type="text" maxlength="100" placeholder="예: 백엔드" /></label>
                    <label class="form-group"><span class="form-label">경력 조건</span><select class="form-control" name="careerType"><option value="">전체</option><option value="NEW">신입</option><option value="CAREER">경력</option><option value="INTERN">인턴</option></select></label>
                    <label class="form-group"><span class="form-label">지역</span><input class="form-control" name="region" type="text" maxlength="100" placeholder="예: 서울" /></label>
                    <label class="form-group"><span class="form-label">등록일 시작</span><input class="form-control" name="postedFrom" type="date" /></label>
                    <label class="form-group"><span class="form-label">등록일 종료</span><input class="form-control" name="postedTo" type="date" /></label>
                    <label class="form-group"><span class="form-label">마감일 시작</span><input class="form-control" name="deadlineFrom" type="date" /></label>
                    <label class="form-group"><span class="form-label">마감일 종료</span><input class="form-control" name="deadlineTo" type="date" /></label>
                    <label class="form-group form-group--full"><span class="form-label">추가 조건</span><input class="form-control" name="extraConditions" type="text" maxlength="500" placeholder="예: 인턴 재택 계약직" /></label>
                </div>
                <div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage/searches">취소</a><button class="jobon-btn jobon-btn--primary" type="submit">저장</button></div>
            </form>
        </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
