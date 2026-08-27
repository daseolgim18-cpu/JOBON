<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>기업 수정 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="company" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>기업 수정</h1><p>등록된 기업 정보를 수정하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">기업명</label><input class="form-control" type="text" placeholder="기업명을 입력하세요"/></div><div class="form-group"><label class="form-label">채용 페이지 URL</label><input class="form-control" type="url" placeholder="https://..."/></div><div class="form-group"><label class="form-label">산업 / 직무</label><input class="form-control" type="text" placeholder="예: IT / 백엔드"/></div><div class="form-group"><label class="form-label">기업 로고</label><input class="form-control" type="file"/></div><div class="form-group form-group--full"><label class="form-label">메모</label><textarea class="form-control" placeholder="기업에 대한 메모를 입력하세요"></textarea></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/company/detail">취소</a><button class="jobon-btn jobon-btn--primary" type="button">저장</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>