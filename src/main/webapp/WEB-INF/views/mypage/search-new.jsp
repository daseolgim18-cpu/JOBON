<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>새 검색어 저장 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>새 검색어 저장</h1><p>자주 사용하는 채용공고 검색 조건을 저장하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group form-group--full"><label class="form-label">검색어 이름</label><input class="form-control" type="text" placeholder="예: 백엔드 신입 서울"/></div><div class="form-group"><label class="form-label">검색 대상</label><select class="form-control"><option>전체 / 기업 / 채용공고</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">키워드</label><input class="form-control" type="text" placeholder="검색 키워드"/></div><div class="form-group"><label class="form-label">직무</label><select class="form-control"><option>직무 선택</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">경력 조건</label><select class="form-control"><option>신입 / 경력 / 인턴</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">지역</label><select class="form-control"><option>지역 선택</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">등록일 시작</label><input class="form-control" type="date"/></div><div class="form-group"><label class="form-label">마감일 종료</label><input class="form-control" type="date"/></div><div class="form-group form-group--full"><label class="form-label">추가 조건</label><input class="form-control" type="text" placeholder="인턴, 계약직, 재택 등"/></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage/searches">취소</a><button class="jobon-btn jobon-btn--primary" type="button">저장</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>