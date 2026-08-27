<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>성장 기록 수정 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="learning" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>성장 기록 수정</h1><p>기존 성장 기록 내용을 수정하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">기록 구분</label><select class="form-control"><option>학습 / 자격증 / 활동</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">학습 날짜</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">기술 키워드</label><input class="form-control" type="text" placeholder="예: Java, Spring Boot"/></div><div class="form-group"><label class="form-label">주제</label><input class="form-control" type="text" placeholder="기록의 주제를 입력하세요"/></div><div class="form-group form-group--full"><label class="form-label">어려웠던 점</label><textarea class="form-control" placeholder="학습 과정에서 어려웠던 점"></textarea></div><div class="form-group form-group--full"><label class="form-label">학습 내용</label><textarea class="form-control" placeholder="학습 내용을 입력하세요"></textarea></div><div class="form-group form-group--full"><label class="form-label">느낀 점 / 활용 계획</label><textarea class="form-control" placeholder="느낀 점 또는 활용 계획"></textarea></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/learning/detail">취소</a><button class="jobon-btn jobon-btn--primary" type="button">저장</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>