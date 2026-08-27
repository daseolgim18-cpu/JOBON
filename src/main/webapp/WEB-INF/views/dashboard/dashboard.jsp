<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>대시보드 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="dashboard" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>대시보드</h1><p>지원 현황, 마감 임박 공고, TODO와 최근 활동을 한 곳에서 확인하세요.</p></div></section><div class="form-grid"><div class="card card--padded"><h2 class="card-title">지원 요약</h2><div class="detail-grid"><div><strong style="font-size:30px;color:#111827">23</strong><p class="muted">전체 지원</p></div><div><span class="badge badge--green">진행 중 12</span> <span class="badge badge--blue">서류 5</span> <span class="badge badge--purple">면접 3</span></div></div></div><div class="card card--padded"><h2 class="card-title">오늘의 TODO</h2><div class="preview-list"><div class="preview-row"><span>자기소개서 수정</span><b>D-1</b></div><div class="preview-row"><span>포트폴리오 점검</span><b>오늘</b></div><div class="preview-row"><span>기업 분석</span><b>D-3</b></div></div></div><div class="card card--padded"><h2 class="card-title">마감 임박 채용공고</h2><p>네이버 · 백엔드 개발자 <span class="badge badge--red">D-2</span></p><p>카카오 · 서버 개발자 <span class="badge badge--orange">D-5</span></p></div><div class="card card--padded"><h2 class="card-title">최근 AI 분석</h2><p>백엔드 개발자 공고 분석 완료</p><a class="text-link" href="${ctx}/ai/job-analysis">분석 보기 →</a></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>