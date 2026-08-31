<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--=========================================================파일
설명=========================================================JOBON 로그인 JSP입니다. 일반 로그인 폼과 Google/Naver/Kakao SNS 로그인
진입 버튼을 제공합니다. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>로그인 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
    <link rel="stylesheet" href="${ctx}/css/member.css" />
  </head>

  <body>
    <c:set var="activeMenu" value="" scope="request" />
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
      <div class="jobon-container">
        <div class="auth-shell">
          <div class="card auth-card">
            <h1 class="auth-title">로그인</h1>
            <p class="auth-desc">JOBON과 함께 취업 준비를 시작하세요.</p>

            <c:if test="${not empty errorMessage}">
              <div class="member-message member-message--error">${errorMessage}</div>
            </c:if>
            <div id="loginMessage" class="member-message member-message--error" hidden></div>

            <form id="loginForm" class="auth-form" novalidate>
              <div><label class="form-label" for="loginId">아이디</label><input class="form-control" id="loginId"
                  name="loginId" autocomplete="username" placeholder="아이디를 입력하세요"></div>
              <div><label class="form-label" for="password">비밀번호</label><input class="form-control" id="password"
                  name="password" type="password" autocomplete="current-password" placeholder="비밀번호를 입력하세요"></div>
              <div class="auth-help"><label><input id="rememberId" type="checkbox"> 아이디 저장</label><a
                  href="${ctx}/member/find-password">비밀번호 찾기</a></div>
              <button class="jobon-btn jobon-btn--primary" type="submit">로그인</button>
            </form>

            <div class="auth-help auth-center"><span>아직 회원이 아니신가요? <a href="${ctx}/join"
                  class="member-link">회원가입</a></span></div>
            <div class="social-divider"><span>간편 로그인</span></div>
            <div class="social-row">
              <a class="social-btn social-btn--google" href="${ctx}/member/google/login">Google</a>
              <a class="social-btn social-btn--naver" href="${ctx}/member/naver/login">Naver</a>
              <a class="social-btn social-btn--kakao" href="${ctx}/member/kakao/login">Kakao</a>
            </div>
          </div>
        </div>
      </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script>window.JOBON_CTX = '${ctx}';</script>
    <script src="${ctx}/js/login.js"></script>
  </body>

</html>