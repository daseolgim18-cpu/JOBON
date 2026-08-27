<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>성장 기록 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="learning" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>성장 기록</h1><p>기술 키워드와 학습 경험을 꾸준히 기록하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/learning/new">기록 등록</a></section><div class="toolbar"><input class="form-control toolbar__grow" type="search" placeholder="주제 또는 기술 키워드 검색"/><select class="form-control" style="max-width:150px"><option>전체</option><option>최근 등록순</option></select></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>구분</th><th>주제</th><th>기술</th><th>학습일</th><th>관리</th></tr></thead><tbody><tr><td><span class="badge badge--green">학습</span></td><td>Spring Boot 복습</td><td>Java, Spring Boot</td><td>2026.08.25</td><td><div class="table-actions"><a class="text-link" href="${ctx}/learning/detail">상세</a><a class="text-link" href="${ctx}/learning/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr><tr><td><span class="badge badge--purple">활동</span></td><td>개인 프로젝트 설계</td><td>ERD, MyBatis</td><td>2026.08.26</td><td><div class="table-actions"><a class="text-link" href="${ctx}/learning/detail">상세</a><a class="text-link" href="${ctx}/learning/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>