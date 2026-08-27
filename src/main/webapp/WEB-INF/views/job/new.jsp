<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>채용공고 등록 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="job" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>채용공고 등록</h1><p>관심 채용공고의 주요 정보를 저장하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">기업</label><select class="form-control"><option>기업을 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">채용 직무</label><input class="form-control" type="text" placeholder="예: 백엔드 개발자"/></div><div class="form-group"><label class="form-label">공고 출처 URL</label><input class="form-control" type="url" placeholder="https://..."/></div><div class="form-group"><label class="form-label">채용공고명</label><input class="form-control" type="text" placeholder="공고명을 입력하세요"/></div><div class="form-group"><label class="form-label">마감일</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">고용 형태</label><select class="form-control"><option>고용 형태를 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group form-group--full"><label class="form-label">채용공고 원문</label><textarea class="form-control" placeholder="공고 원문을 입력하세요"></textarea></div><div class="form-group form-group--full"><label class="form-label">메모</label><textarea class="form-control" placeholder="필요한 메모를 입력하세요"></textarea></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/job/list">취소</a><button class="jobon-btn jobon-btn--primary" type="button">등록</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>