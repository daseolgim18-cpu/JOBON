<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>프로필 수정 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>프로필 수정</h1><p>닉네임, 한 줄 소개, 관심 직무와 희망 근무지를 수정하세요.</p></div></section><div class="card card--padded"><div class="form-grid"><div class="form-group"><label class="form-label">프로필 이미지</label><input class="form-control" type="file"/></div><div class="form-group"><label class="form-label">닉네임</label><input class="form-control" type="text" placeholder="닉네임"/></div><div class="form-group form-group--full"><label class="form-label">한 줄 소개</label><input class="form-control" type="text" placeholder="한 줄 소개"/></div><div class="form-group"><label class="form-label">관심 직무</label><select class="form-control"><option>관심 직무</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div><div class="form-group"><label class="form-label">희망 근무지</label><select class="form-control"><option>희망 근무지</option><option>선택 항목 1</option><option>선택 항목 2</option></select></div></div><div class="form-actions"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage">취소</a><button class="jobon-btn jobon-btn--primary" type="button">저장하기</button></div></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>