<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1"/>
<title>프로젝트 경험 상세 | JOBON</title>
<link rel="stylesheet" href="${ctx}/css/common.css"/>

</head>
<body>
<c:set var="activeMenu" value="project" scope="request"/>
<jsp:include page="/WEB-INF/views/common/header.jsp"/>
<main class="jobon-page"><div class="jobon-container">
<section class="page-heading"><div><h1>프로젝트 경험 상세</h1><p>역할, 사용 기술, 수행 경험과 성과를 확인하세요.</p></div></section><div class="card card--padded"><div class="form-actions" style="margin-top:0;padding-top:0;border-top:0"><a class="jobon-btn jobon-btn--ghost" href="${ctx}/project/edit">수정</a><button class="jobon-btn jobon-btn--danger" type="button">삭제</button></div><dl class="detail-list"><div><dt>프로젝트명</dt><dd>JOBON</dd></div><div><dt>소속</dt><dd>개인 프로젝트</dd></div><div><dt>역할</dt><dd>Full-Stack 개발</dd></div><div><dt>기간</dt><dd>2026.08 ~ 진행 중</dd></div><div><dt>주요 기술</dt><dd>Java 21 · Spring Boot · JSP · MyBatis · Oracle</dd></div><div><dt>프로젝트 URL</dt><dd>GitHub Repository</dd></div><div><dt>수행 경험</dt><dd>기업 → 채용공고 → 지원 → TODO → 성장 기록 → 프로젝트 경험 → AI 분석으로 이어지는 취업 준비 흐름을 설계했습니다.</dd></div></dl><h2 class="section-title" style="margin-top:28px">주요 기능 / 트러블슈팅</h2><p class="muted">PROJECT_FEATURE, PROJECT_TROUBLE 데이터가 연결될 영역입니다.</p></div>
</div></main>
<jsp:include page="/WEB-INF/views/common/footer.jsp"/>
</body></html>