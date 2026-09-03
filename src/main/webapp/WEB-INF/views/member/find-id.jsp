<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko"><head><meta charset="UTF-8"><meta name="viewport" content="width=device-width, initial-scale=1">
<title>아이디 찾기 | JOBON</title><link rel="stylesheet" href="${ctx}/css/common.css"></head>
<body><c:set var="activeMenu" value="" scope="request"/><jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container"><section class="page-heading"><div><h1>아이디 찾기</h1><p>가입 시 등록한 이름, 이메일, 휴대폰 번호를 입력해주세요.</p></div></section>
<c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
<c:choose><c:when test="${findIdSuccess}"><div class="auth-shell"><div class="card auth-card"><div class="auth-form"><div class="alert alert--success">회원님의 아이디는 <strong>${foundLoginId}</strong> 입니다.</div><a class="jobon-btn jobon-btn--primary" href="${ctx}/login">로그인하러 가기</a><a class="jobon-btn jobon-btn--ghost" href="${ctx}/member/find-password">비밀번호 찾기</a></div></div></div></c:when>
<c:otherwise><div class="auth-shell"><div class="card auth-card"><form class="auth-form" method="post" action="${ctx}/member/find-id"><input class="form-control" name="name" maxlength="50" placeholder="이름" required><input class="form-control" name="email" type="email" maxlength="150" placeholder="가입 이메일" required><input class="form-control" name="phone" maxlength="20" placeholder="휴대폰 번호" required><button class="jobon-btn jobon-btn--primary" type="submit">아이디 찾기</button><a class="jobon-btn jobon-btn--ghost" href="${ctx}/login">로그인 페이지로 돌아가기</a></form></div></div></c:otherwise></c:choose>
</div></main><jsp:include page="/WEB-INF/views/common/footer.jsp"/></body></html>
