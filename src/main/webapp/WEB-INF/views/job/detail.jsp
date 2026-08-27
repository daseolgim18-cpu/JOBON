<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>채용공고 상세 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="job" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>채용공고 상세</h1><p>등록한 채용공고의 상세 정보를 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/job/edit">수정</a><button class="jobon-btn jobon-btn--danger" type="button">삭제</button></div><dl class="detail-list"><div><dt>기업</dt><dd>네이버</dd></div><div><dt>공고명</dt><dd>백엔드 개발자 채용</dd></div><div><dt>직무</dt><dd>백엔드 개발</dd></div><div><dt>마감일</dt><dd>2026.09.02</dd></div><div><dt>출처 URL</dt><dd>https://example.com/job</dd></div><div><dt>요구 기술</dt><dd>Java · Spring · Oracle</dd></div><div><dt>메모</dt><dd>자격요건과 프로젝트 경험 연결 예정</dd></div></dl></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>