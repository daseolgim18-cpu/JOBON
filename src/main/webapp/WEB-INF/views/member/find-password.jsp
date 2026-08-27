<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>비밀번호 찾기 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>비밀번호 찾기</h1><p>가입한 아이디, 이름, 이메일을 입력하면 임시 비밀번호 발송 절차를 진행합니다.</p></div></section><div class="auth-shell"><div class="card auth-card"><div class="auth-form"><input class="form-control" placeholder="아이디"><input class="form-control" placeholder="이름"><input class="form-control" type="email" placeholder="이메일"><button class="jobon-btn jobon-btn--primary">임시 비밀번호 받기</button><a class="jobon-btn jobon-btn--ghost" href="${ctx}/login">로그인 페이지로 이동</a></div></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>