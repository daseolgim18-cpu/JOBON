<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>AI 분석 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="ai" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>AI 분석</h1><p>채용공고를 선택하고 주요 업무, 자격요건, 기술 요구사항을 분석하세요.</p></div></section><div class="toolbar"><select class="form-control toolbar__grow"><option>분석할 채용공고를 선택하세요</option><option>네이버 - 백엔드 개발자 채용</option></select><button class="jobon-btn jobon-btn--primary">AI 분석 요청</button></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>채용공고</th><th>요청일</th><th>분석 요약</th><th>상태</th><th>관리</th></tr></thead><tbody><tr><td>네이버 - 백엔드 개발자</td><td>2026.08.27</td><td>Spring 기반 서버 개발 역량 중심</td><td><span class="badge badge--green">완료</span></td><td><a class="text-link" href="${ctx}/ai/job-analysis">상세 보기</a></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>