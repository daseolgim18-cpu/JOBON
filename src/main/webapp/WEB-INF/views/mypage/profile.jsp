<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>마이페이지 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<div class="mypage-layout"><aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a></aside><section><section class="page-heading"><div><h1>마이페이지</h1><p>프로필과 JOBON 활동 정보를 관리하세요.</p></div><a class="jobon-btn jobon-btn--primary" href="${ctx}/mypage/profile/edit">프로필 수정</a></section><div class="card card--padded"><div style="display:flex;gap:20px;align-items:center"><div style="width:76px;height:76px;border-radius:50%;background:var(--jobon-green-soft);display:grid;place-items:center;color:var(--jobon-green-dark);font-size:28px">D</div><div><h2 style="margin:0 0 6px;color:#111827">김다설</h2><p class="muted" style="margin:0">백엔드 개발자를 준비하며 성장 기록을 쌓고 있습니다.</p></div></div><dl class="detail-list" style="margin-top:24px"><div><dt>관심 직무</dt><dd>백엔드 개발</dd></div><div><dt>희망 근무지</dt><dd>서울 / 경기</dd></div><div><dt>이메일</dt><dd>sample@jobon.com</dd></div></dl></div></section></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>