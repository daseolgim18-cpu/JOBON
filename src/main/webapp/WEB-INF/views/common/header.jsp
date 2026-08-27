<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<header class="jobon-header">
  <div class="jobon-header__inner">
    <a class="jobon-logo" href="${ctx}/main" aria-label="JOBON 메인">JOBON</a>

    <button class="jobon-nav-toggle" type="button" aria-label="메뉴 열기" aria-expanded="false">
      <span></span><span></span><span></span>
    </button>

    <nav class="jobon-nav" aria-label="주요 메뉴">
      <a class="${activeMenu eq 'dashboard' ? 'is-active' : ''}" href="${ctx}/dashboard">대시보드</a>
      <a class="${activeMenu eq 'company' ? 'is-active' : ''}" href="${ctx}/company/list">기업 관리</a>
      <a class="${activeMenu eq 'job' ? 'is-active' : ''}" href="${ctx}/job/list">채용공고</a>
      <a class="${activeMenu eq 'apply' ? 'is-active' : ''}" href="${ctx}/apply/list">지원 현황</a>
      <a class="${activeMenu eq 'todo' ? 'is-active' : ''}" href="${ctx}/todo/list">TODO</a>
      <a class="${activeMenu eq 'learning' ? 'is-active' : ''}" href="${ctx}/learning/list">성장 기록</a>
      <a class="${activeMenu eq 'project' ? 'is-active' : ''}" href="${ctx}/project/list">프로젝트 경험</a>
      <a class="${activeMenu eq 'ai' ? 'is-active' : ''}" href="${ctx}/ai/analysis">AI 분석</a>
    </nav>

    <form class="jobon-search" action="${ctx}/search/result" method="get">
      <input type="search" name="keyword" placeholder="기업, 공고, 키워드로 검색하세요" aria-label="통합 검색" />
      <button type="submit" aria-label="검색">⌕</button>
    </form>

    <div class="jobon-account">
      <c:choose>
        <c:when test="${not empty sessionScope.loginMember}">
          <a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage">마이페이지</a>
          <a class="jobon-btn jobon-btn--primary" href="${ctx}/logout">로그아웃</a>
        </c:when>
        <c:otherwise>
          <a class="jobon-btn jobon-btn--ghost" href="${ctx}/login">로그인</a>
          <a class="jobon-btn jobon-btn--primary" href="${ctx}/join">회원가입</a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</header>
