<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>AI 채용공고 분석 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="ai" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>AI 채용공고 분석</h1><p>채용공고의 주요 업무, 자격요건, 우대사항과 요구 역량을 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/ai/analysis">수정</a></div><dl class="detail-list"><div><dt>채용공고</dt><dd>네이버 - 백엔드 개발자</dd></div><div><dt>분석 상태</dt><dd>완료</dd></div><div><dt>주요 업무</dt><dd>서버 API 설계 및 개발, 서비스 운영</dd></div><div><dt>자격 요건</dt><dd>Java/Spring 기반 개발 경험</dd></div><div><dt>우대 사항</dt><dd>대용량 트래픽 처리 경험</dd></div><div><dt>요구 역량</dt><dd>문제 해결 · 협업 · 백엔드 설계</dd></div><div><dt>요구 기술</dt><dd>Java · Spring · SQL · Git</dd></div></dl><div class="form-actions"><button class="jobon-btn jobon-btn--ghost">다시 분석</button><a class="jobon-btn jobon-btn--primary" href="${ctx}/ai/analysis">목록으로</a></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>