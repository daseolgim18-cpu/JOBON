<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>성장 기록 상세 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="learning" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>성장 기록 상세</h1><p>학습 과정과 기술 키워드를 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/learning/edit">수정</a><button class="jobon-btn jobon-btn--danger" type="button">삭제</button></div><dl class="detail-list"><div><dt>구분</dt><dd>학습</dd></div><div><dt>주제</dt><dd>Spring Boot 복습</dd></div><div><dt>학습일</dt><dd>2026.08.25</dd></div><div><dt>기술 키워드</dt><dd>Java · Spring Boot</dd></div><div><dt>학습 내용</dt><dd>Spring Boot 프로젝트 구조와 MVC 흐름을 복습했습니다.</dd></div><div><dt>어려웠던 점</dt><dd>JSP View Resolver와 정적 리소스 경로를 정리했습니다.</dd></div><div><dt>활용 계획</dt><dd>JOBON 프로젝트 공통 화면 구성에 적용합니다.</dd></div></dl></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>