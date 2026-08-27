<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>기업 관리 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="company" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>기업 관리</h1><p>관심 있는 기업의 기본 정보와 채용 페이지를 관리하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/company/new">기업 등록</a></section><div class="toolbar"><input class="form-control toolbar__grow" type="search" placeholder="기업명을 입력하세요"/><select class="form-control" style="max-width:150px"><option>전체</option><option>최근 등록순</option></select></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>기업명</th><th>구분</th><th>산업 / 직무</th><th>등록일</th><th>관리</th></tr></thead><tbody><tr><td>네이버</td><td><span class="badge badge--green">대기업</span></td><td>IT · 백엔드</td><td>2026.08.23</td><td><div class="table-actions"><a class="text-link" href="${ctx}/company/detail">상세</a><a class="text-link" href="${ctx}/company/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr><tr><td>카카오</td><td><span class="badge badge--blue">대기업</span></td><td>IT · 플랫폼</td><td>2026.08.22</td><td><div class="table-actions"><a class="text-link" href="${ctx}/company/detail">상세</a><a class="text-link" href="${ctx}/company/edit">수정</a><button class="text-link danger">삭제</button></div></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>