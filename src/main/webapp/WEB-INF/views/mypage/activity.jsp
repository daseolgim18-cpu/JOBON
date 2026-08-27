<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>활동 내역 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>활동 내역</h1><p>JOBON에서 수행한 주요 활동을 시간순으로 확인하세요.</p></div></section><div class="mypage-layout"><aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a></aside><section class="card card--padded"><div class="toolbar"><select class="form-control" style="max-width:170px"><option>전체 활동</option><option>기업</option><option>채용공고</option><option>TODO</option></select></div><div class="preview-list"><div class="preview-row"><span>채용공고 · 네이버 백엔드 개발자 공고 등록</span><b>2시간 전</b></div><div class="preview-row"><span>TODO · 자기소개서 수정 등록</span><b>어제</b></div><div class="preview-row"><span>성장 기록 · Spring Boot 복습 작성</span><b>2일 전</b></div></div></section></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>