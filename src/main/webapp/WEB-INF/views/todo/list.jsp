<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>TODO | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="todo" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>TODO</h1><p>할 일을 등록하고 우선순위와 진행 상태를 관리하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/todo/new">TODO 등록</a></section><div class="toolbar"><input class="form-control toolbar__grow" type="search" placeholder="할 일을 검색하세요"/><select class="form-control" style="max-width:150px"><option>전체</option><option>최근 등록순</option></select></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>할 일</th><th>우선순위</th><th>마감일</th><th>관련 기업</th><th>상태</th><th>관리</th></tr></thead><tbody><tr><td>자기소개서 수정</td><td><span class="badge badge--red">높음</span></td><td>2026.08.28</td><td>네이버</td><td><span class="badge badge--orange">진행 중</span></td><td><div class="table-actions"><a class="text-link" href="${ctx}/todo/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr><tr><td>포트폴리오 점검</td><td><span class="badge badge--blue">보통</span></td><td>2026.08.30</td><td>카카오</td><td><span class="badge badge--green">완료</span></td><td><div class="table-actions"><a class="text-link" href="${ctx}/todo/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>