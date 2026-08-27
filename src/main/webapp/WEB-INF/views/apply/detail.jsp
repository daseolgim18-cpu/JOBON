<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>지원 현황 상세 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="apply" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>지원 현황 상세</h1><p>지원 상태, 일정, 연결된 공고와 TODO를 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/apply/edit">수정</a><button class="jobon-btn jobon-btn--danger" type="button">삭제</button></div><dl class="detail-list"><div><dt>기업</dt><dd>네이버</dd></div><div><dt>공고</dt><dd>백엔드 개발자 채용</dd></div><div><dt>지원 상태</dt><dd>서류 심사</dd></div><div><dt>지원일</dt><dd>2026.08.24</dd></div><div><dt>다음 일정</dt><dd>2026.08.30</dd></div><div><dt>메모</dt><dd>서류 결과 대기 중</dd></div></dl><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/job/detail">채용공고 보기</a><a class="jobon-btn jobon-btn--ghost" href="${ctx}/todo/new">TODO 생성</a><a class="jobon-btn jobon-btn--soft" href="${ctx}/ai/job-analysis">AI 분석 결과 보기</a></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>