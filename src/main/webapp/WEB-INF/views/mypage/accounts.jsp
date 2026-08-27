<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>연동 계정 관리 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>연동 계정 관리</h1><p>Google, Naver, Kakao 계정의 연동 상태를 관리하세요.</p></div></section><div class="mypage-layout"><aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a></aside><section class="card card--padded"><table class="data-table"><thead><tr><th>서비스</th><th>상태</th><th>이메일</th><th>관리</th></tr></thead><tbody><tr><td>Google</td><td><span class="badge badge--green">연동</span></td><td>sample@gmail.com</td><td><button class="text-link danger">연동 해제</button></td></tr><tr><td>Naver</td><td><span class="badge badge--blue">미연동</span></td><td>-</td><td><button class="text-link">연동하기</button></td></tr><tr><td>Kakao</td><td><span class="badge badge--blue">미연동</span></td><td>-</td><td><button class="text-link">연동하기</button></td></tr></tbody></table></section></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>