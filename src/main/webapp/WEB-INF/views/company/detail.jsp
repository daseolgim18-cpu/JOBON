<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>기업 상세 보기 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="company" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>기업 상세 보기</h1><p>등록한 기업의 상세 정보를 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/company/edit">수정</a><button class="jobon-btn jobon-btn--danger" type="button">삭제</button></div><dl class="detail-list"><div><dt>기업명</dt><dd>네이버</dd></div><div><dt>기업 구분</dt><dd>대기업</dd></div><div><dt>산업 / 직무</dt><dd>IT · 백엔드</dd></div><div><dt>채용 페이지</dt><dd>https://recruit.navercorp.com</dd></div><div><dt>메모</dt><dd>백엔드 개발 직무 중심으로 공고 확인 예정</dd></div></dl><h2 class="section-title" style="margin-top:28px">연결된 채용공고</h2><p class="muted">이 기업에 등록된 채용공고가 여기에 표시됩니다.</p></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>