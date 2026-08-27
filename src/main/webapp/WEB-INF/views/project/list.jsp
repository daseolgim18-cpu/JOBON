<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>프로젝트 경험 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="project" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>프로젝트 경험</h1><p>프로젝트별 역할, 기술, 수행 경험과 성과를 기록하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/project/new">프로젝트 등록</a></section><div class="toolbar"><input class="form-control toolbar__grow" type="search" placeholder="프로젝트명을 검색하세요"/><select class="form-control" style="max-width:150px"><option>전체</option><option>최근 등록순</option></select></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>프로젝트</th><th>기간</th><th>역할</th><th>주요 기술</th><th>관리</th></tr></thead><tbody><tr><td>ODITJI</td><td>2026.07 ~ 2026.08</td><td>Full-Stack</td><td>Java, Spring MVC, Oracle</td><td><div class="table-actions"><a class="text-link" href="${ctx}/project/detail">상세</a><a class="text-link" href="${ctx}/project/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr><tr><td>JOBON</td><td>2026.08 ~ 진행 중</td><td>Full-Stack</td><td>Spring Boot, MyBatis</td><td><div class="table-actions"><a class="text-link" href="${ctx}/project/detail">상세</a><a class="text-link" href="${ctx}/project/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>