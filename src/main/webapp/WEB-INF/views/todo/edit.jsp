<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>TODO 수정 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="todo" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>TODO 수정</h1><p>등록한 할 일의 일정과 상태를 수정하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">할 일</label><input class="form-control" type="text" placeholder="해야 할 일을 입력하세요"/></div><div class="form-group"><label class="form-label">우선순위</label><select class="form-control"><option>우선순위를 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">마감일</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">관련 기업</label><select class="form-control"><option>기업을 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">관련 채용공고</label><select class="form-control"><option>채용공고를 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">진행 상태</label><select class="form-control"><option>상태를 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group form-group--full"><label class="form-label">메모</label><textarea class="form-control" placeholder="필요한 메모를 입력하세요"></textarea></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/todo/list">취소</a><button class="jobon-btn jobon-btn--primary" type="button">저장</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>