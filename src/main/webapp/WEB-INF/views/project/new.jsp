<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>프로젝트 경험 등록 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="project" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>프로젝트 경험 등록</h1><p>취업과 자기소개서에 활용할 프로젝트 경험을 정리하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">프로젝트명</label><input class="form-control" type="text" placeholder="프로젝트명을 입력하세요"/></div><div class="form-group"><label class="form-label">기업 / 소속</label><input class="form-control" type="text" placeholder="기업 또는 소속 정보"/></div><div class="form-group"><label class="form-label">담당 역할</label><select class="form-control"><option>역할을 선택하세요</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">시작일</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">종료일</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">주요 기술</label><input class="form-control" type="text" placeholder="예: Java, Spring Boot, Oracle"/></div><div class="form-group form-group--full"><label class="form-label">프로젝트 내용 / 수행 경험</label><textarea class="form-control" placeholder="프로젝트에서 수행한 경험을 입력하세요"></textarea></div><div class="form-group form-group--full"><label class="form-label">프로젝트 링크</label><input class="form-control" type="url" placeholder="https://github.com/..."/></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/project/list">취소</a><button class="jobon-btn jobon-btn--primary" type="button">등록</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>