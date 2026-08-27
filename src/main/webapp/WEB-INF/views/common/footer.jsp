<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<footer class="jobon-footer">
  <div class="jobon-footer__inner">
    <section class="jobon-footer__brand">
      <strong>JOBON</strong>
      <p>취업 준비의 모든 과정을 한 곳에서 관리하고,<br>성장하는 당신의 커리어 여정을 함께합니다.</p>
    </section>

    <section class="jobon-footer__links">
      <div>
        <strong>주요 메뉴</strong>
        <a href="${ctx}/dashboard">대시보드</a>
        <a href="${ctx}/company/list">기업 관리</a>
        <a href="${ctx}/job/list">채용공고</a>
        <a href="${ctx}/apply/list">지원 현황</a>
        <a href="${ctx}/todo/list">TODO</a>
      </div>
      <div>
        <strong>성장/분석</strong>
        <a href="${ctx}/learning/list">성장 기록</a>
        <a href="${ctx}/project/list">프로젝트 경험</a>
        <a href="${ctx}/ai/analysis">AI 분석</a>
      </div>
      <div>
        <strong>계정</strong>
        <a href="${ctx}/mypage">내 프로필</a>
        <a href="${ctx}/mypage/profile/edit">프로필 수정</a>
        <a href="${ctx}/mypage/password">비밀번호 변경</a>
        <a href="${ctx}/mypage/withdraw">회원 탈퇴</a>
      </div>
    </section>
  </div>
  <div class="jobon-footer__bottom">
    <span>© 2026 JOBON. All rights reserved.</span>
    <span>취업 준비 통합 관리 서비스</span>
  </div>
</footer>
<script src="${ctx}/js/common.js"></script>
