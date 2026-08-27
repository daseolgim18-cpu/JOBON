<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>자소서 경험 TOP3 추천 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="ai" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>자소서 경험 TOP3 추천</h1><p>채용공고 요구사항과 저장된 프로젝트 경험을 연결해 활용도가 높은 경험을 추천합니다.</p></div></section><div class="card card--padded"><p class="muted">대상 공고 · 네이버 백엔드 개발자 채용</p><div style="display:grid;gap:14px;margin-top:20px"><div class="card card--padded"><span class="badge badge--green">TOP 1</span><h3>ODITJI 회원·인증 / 주문·결제 경험</h3><p>Spring MVC와 Oracle 기반으로 실제 서비스 흐름을 구현한 경험이 공고의 서버 개발 역량과 연결됩니다.</p></div><div class="card card--padded"><span class="badge badge--blue">TOP 2</span><h3>JOBON 통합 취업 관리 서비스</h3><p>도메인 설계부터 CRUD, MyBatis, AI 분석까지 직접 설계한 경험을 활용할 수 있습니다.</p></div><div class="card card--padded"><span class="badge badge--purple">TOP 3</span><h3>Python + Oracle 성적관리</h3><p>데이터 모델링과 DB 연동, 기능별 분석 경험을 강조할 수 있습니다.</p></div></div><div class="form-actions"><button class="jobon-btn jobon-btn--ghost">다시 추천</button><button class="jobon-btn jobon-btn--primary">저장하기</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>