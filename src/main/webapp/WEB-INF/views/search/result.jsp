<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>통합 검색 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
    <link rel="stylesheet" href="${ctx}/css/domain.css" />
</head>
<body>
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page"><div class="jobon-container">
        <section class="page-heading"><div><h1>통합 검색</h1><p>내가 저장한 기업과 채용공고에서 검색합니다.</p></div></section>
        <form class="card card--padded" method="get" action="${ctx}/search/result">
            <div class="form-grid">
                <label class="form-group--full"><span class="form-label">검색어</span><input class="form-control" name="keyword" value="${keyword}" placeholder="기업명, 공고명, 직무, 공고 원문 키워드"></label>
                <label><span class="form-label">대상</span><select class="form-control" name="targetType"><option value="ALL" ${targetType eq 'ALL'?'selected':''}>전체</option><option value="COMPANY" ${targetType eq 'COMPANY'?'selected':''}>기업</option><option value="JOB" ${targetType eq 'JOB'?'selected':''}>채용공고</option></select></label>
                <label><span class="form-label">직무</span><input class="form-control" name="jobRole" value="${jobRole}" placeholder="백엔드"></label>
                <label><span class="form-label">경력</span><select class="form-control" name="careerType"><option value="">전체</option><option value="NEW" ${careerType eq 'NEW'?'selected':''}>신입</option><option value="CAREER" ${careerType eq 'CAREER'?'selected':''}>경력</option><option value="INTERN" ${careerType eq 'INTERN'?'selected':''}>인턴</option></select></label>
                <label><span class="form-label">지역</span><input class="form-control" name="region" value="${region}" placeholder="서울"></label>
                <label><span class="form-label">등록일 시작</span><input class="form-control" type="date" name="postedFrom" value="${postedFrom}"></label>
                <label><span class="form-label">등록일 종료</span><input class="form-control" type="date" name="postedTo" value="${postedTo}"></label>
                <label><span class="form-label">마감일 시작</span><input class="form-control" type="date" name="deadlineFrom" value="${deadlineFrom}"></label>
                <label><span class="form-label">마감일 종료</span><input class="form-control" type="date" name="deadlineTo" value="${deadlineTo}"></label>
                <label class="form-group--full"><span class="form-label">추가 조건</span><input class="form-control" name="extraConditions" value="${extraConditions}" placeholder="인턴 재택 계약직"></label>
            </div>
            <div class="form-actions"><button class="jobon-btn jobon-btn--primary" type="submit">검색</button></div>
        </form>
        <p class="muted mt20">총 ${totalCount}건의 결과</p>
        <c:if test="${targetType ne 'JOB'}"><section class="card card--padded mt20"><div class="nested-head"><h3>기업 (${companyResults.size()})</h3></div><c:forEach var="c" items="${companyResults}"><div class="dashboard-row"><a class="dashboard-row__title" href="${ctx}/company/detail?id=${c.companyId}"><strong>${c.companyName}</strong> · ${c.industry} · ${c.jobField}</a><span class="dashboard-row__meta">기업 상세</span></div></c:forEach><c:if test="${empty companyResults}"><p class="muted">검색된 기업이 없습니다.</p></c:if></section></c:if>
        <c:if test="${targetType ne 'COMPANY'}"><section class="card card--padded mt20"><div class="nested-head"><h3>채용공고 (${jobResults.size()})</h3></div><c:forEach var="j" items="${jobResults}"><div class="dashboard-row"><a class="dashboard-row__title" href="${ctx}/job/detail?id=${j.jobId}"><strong>${j.companyName}</strong> · ${j.title}<br><span class="muted">${j.jobRole} · ${j.region}</span></a><span class="dashboard-row__meta">마감 ${j.deadline}</span></div></c:forEach><c:if test="${empty jobResults}"><p class="muted">검색된 채용공고가 없습니다.</p></c:if></section></c:if>
    </div></main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
