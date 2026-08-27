<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />
  <title>JOBON | 취업 준비 통합 관리</title>
  <link rel="stylesheet" href="${ctx}/css/common.css" />
  <link rel="stylesheet" href="${ctx}/css/main.css" />
</head>
<body>
  <jsp:include page="/WEB-INF/views/common/header.jsp" />

  <main class="jobon-page">
    <section class="main-hero">
      <div class="main-hero__inner">
        <div>
          <h1>취업 준비를 한 곳에서,<em>JOBON</em></h1>
          <p>목표 기업 관리부터 지원 현황, 할 일 관리, 성장 기록, 프로젝트 경험까지 취업 준비의 모든 과정을 체계적으로 관리하세요.</p>
        </div>
        <div class="main-hero__visual">
          <img src="${ctx}/images/jobon-hero.svg" alt="JOBON 대시보드가 표시된 노트북 일러스트" />
        </div>
      </div>
    </section>

    <section class="main-section">
      <h2 class="main-section__title">핵심 기능 바로가기</h2>
      <div class="quick-grid">
        <a class="quick-card" href="${ctx}/company/list"><div class="quick-card__icon">▦</div><h3>기업 관리</h3><p>관심 기업을 등록하고 정보를 체계적으로 관리</p><span>→</span></a>
        <a class="quick-card" href="${ctx}/job/list"><div class="quick-card__icon">▤</div><h3>채용공고</h3><p>관심 채용공고를 모아보고 주요 정보를 확인</p><span>→</span></a>
        <a class="quick-card" href="${ctx}/apply/list"><div class="quick-card__icon">▣</div><h3>지원 현황</h3><p>기업별 지원 진행 상황을 한눈에 확인</p><span>→</span></a>
        <a class="quick-card" href="${ctx}/todo/list"><div class="quick-card__icon">✓</div><h3>TODO</h3><p>해야 할 일을 정리하고 우선순위를 관리</p><span>→</span></a>
        <a class="quick-card" href="${ctx}/learning/list"><div class="quick-card__icon">↗</div><h3>성장 기록</h3><p>학습, 기술, 소셜 등 성장 과정을 기록</p><span>→</span></a>
        <a class="quick-card" href="${ctx}/project/list"><div class="quick-card__icon">▱</div><h3>프로젝트 경험</h3><p>프로젝트 경험과 성과를 체계적으로 관리</p><span>→</span></a>
      </div>
    </section>

    <section class="main-section">
      <div class="main-bottom-grid">
        <div class="card flow-card">
          <h2 class="main-section__title">서비스 이용 흐름</h2>
          <div class="flow-steps">
            <div class="flow-step"><small>STEP 01</small><div class="flow-step__icon">◎</div><h4>목표 기업 정리</h4><p>목표 기업의 지원 분야와 일정을 정리해요.</p></div>
            <div class="flow-step"><small>STEP 02</small><div class="flow-step__icon">⌕</div><h4>채용공고 확인</h4><p>기업별 채용공고 정보를 수집하고 정리해요.</p></div>
            <div class="flow-step"><small>STEP 03</small><div class="flow-step__icon">▣</div><h4>지원 일정 관리</h4><p>전형 상태와 다음 일정을 한눈에 확인해요.</p></div>
            <div class="flow-step"><small>STEP 04</small><div class="flow-step__icon">☑</div><h4>할 일 관리</h4><p>일정별 계획을 세우고 우선순위로 관리해요.</p></div>
            <div class="flow-step"><small>STEP 05</small><div class="flow-step__icon">↗</div><h4>성장 기록</h4><p>학습과 경험을 기록하고 꾸준히 성장해요.</p></div>
          </div>
        </div>

        <div class="card dashboard-preview">
          <h2 class="main-section__title">대시보드 미리보기</h2>
          <div class="dashboard-grid">
            <div class="preview-panel"><h4>오늘의 할 일</h4><div class="preview-list"><div class="preview-row"><span>서류 제출</span><b>2건</b></div><div class="preview-row"><span>면접 준비</span><b>1건</b></div><div class="preview-row"><span>자기소개서 수정</span><b>3건</b></div></div></div>
            <div class="preview-panel"><h4>지원 현황 요약</h4><div class="donut"></div></div>
            <div class="preview-panel"><h4>이번 주 할 일</h4><div class="preview-list"><div class="preview-row"><span>이력서 업데이트</span><b>✓</b></div><div class="preview-row"><span>포트폴리오 확인</span><b>•</b></div><div class="preview-row"><span>면접 준비</span><b>•</b></div></div></div>
          </div>
          <div class="preview-footer"><span>최근 활동 · 한국자동차 서류 결과가 업데이트 되었어요.</span><a href="${ctx}/dashboard">전체 보기 →</a></div>
        </div>
      </div>
    </section>
  </main>

  <jsp:include page="/WEB-INF/views/common/footer.jsp" />
</body>
</html>
