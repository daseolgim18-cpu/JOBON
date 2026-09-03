<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>비밀번호 찾기 | JOBON</title><link rel="stylesheet" href="${ctx}/css/common.css">
</head>
<body>
<c:set var="activeMenu" value="" scope="request"/><jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>비밀번호 찾기</h1><p>가입한 회원정보가 일치하면 등록된 이메일로 임시 비밀번호를 발송합니다.</p></div></section>
<c:if test="${not empty successMessage}"><div class="alert alert--success">${successMessage}</div></c:if>
<c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
<div class="auth-shell"><div class="card auth-card"><form class="auth-form" method="post" action="${ctx}/member/find-password">
<input class="form-control" name="loginId" maxlength="50" placeholder="아이디" required>
<input class="form-control" name="name" maxlength="50" placeholder="이름" required>
<input class="form-control" name="email" type="email" maxlength="150" placeholder="가입 이메일" required>
<button class="jobon-btn jobon-btn--primary" type="submit">임시 비밀번호 받기</button>
<a class="jobon-btn jobon-btn--ghost" href="${ctx}/member/find-id">아이디 찾기</a>
<a class="jobon-btn jobon-btn--ghost" href="${ctx}/login">로그인 페이지로 이동</a>
</form></div></div></div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>
