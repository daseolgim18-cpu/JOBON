<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>지원 현황 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="apply" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>지원 현황</h1><p>지원 상태와 다음 일정을 확인하고 관리하세요.</p></div></section><div class="toolbar"><input class="form-control toolbar__grow" type="search" placeholder="기업명을 입력하세요"/><select class="form-control" style="max-width:150px"><option>전체</option><option>최근 등록순</option></select></div><div class="card table-wrap"><table class="data-table"><thead><tr><th>기업</th><th>공고명</th><th>지원일</th><th>상태</th><th>다음 일정</th><th>관리</th></tr></thead><tbody><tr><td>네이버</td><td>백엔드 개발자</td><td>2026.08.24</td><td><span class="badge badge--blue">서류 심사</span></td><td>2026.08.30</td><td><a class="text-link" href="${ctx}/apply/detail">상세</a></td></tr><tr><td>카카오</td><td>서버 개발자</td><td>2026.08.20</td><td><span class="badge badge--purple">면접</span></td><td>2026.08.29</td><td><a class="text-link" href="${ctx}/apply/detail">상세</a></td></tr></tbody></table></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>