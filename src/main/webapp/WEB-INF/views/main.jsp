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
      <link rel="stylesheet" href="${ctx}/css/main.css?v=20260902-4" />
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
            <a class="quick-card" href="${ctx}/company/list">
              <div class="quick-card__icon">▦</div>
              <h3>기업 관리</h3>
              <p>관심 기업을 등록하고 정보를 체계적으로 관리</p><span>→</span>
            </a>
            <a class="quick-card" href="${ctx}/job/list">
              <div class="quick-card__icon">▤</div>
              <h3>채용공고</h3>
              <p>관심 채용공고를 모아보고 주요 정보를 확인</p><span>→</span>
            </a>
            <a class="quick-card" href="${ctx}/apply/list">
              <div class="quick-card__icon">▣</div>
              <h3>지원 현황</h3>
              <p>기업별 지원 진행 상황을 한눈에 확인</p><span>→</span>
            </a>
            <a class="quick-card" href="${ctx}/todo/list">
              <div class="quick-card__icon">✓</div>
              <h3>TODO</h3>
              <p>해야 할 일을 정리하고 우선순위를 관리</p><span>→</span>
            </a>
            <a class="quick-card" href="${ctx}/learning/list">
              <div class="quick-card__icon">↗</div>
              <h3>성장 기록</h3>
              <p>학습, 기술, 소셜 등 성장 과정을 기록</p><span>→</span>
            </a>
            <a class="quick-card" href="${ctx}/project/list">
              <div class="quick-card__icon">▱</div>
              <h3>프로젝트 경험</h3>
              <p>프로젝트 경험과 성과를 체계적으로 관리</p><span>→</span>
            </a>
          </div>
        </section>

        <section class="main-section">
          <div class="main-bottom-grid">
            <div class="card flow-card">
              <h2 class="main-section__title">서비스 이용 흐름</h2>
              <div class="flow-steps">
                <div class="flow-step"><small>STEP 01</small>
                  <div class="flow-step__icon">◎</div>
                  <h4>목표 기업 정리</h4>
                  <p>목표 기업의 지원 분야와 일정을 정리해요.</p>
                </div>
                <div class="flow-step"><small>STEP 02</small>
                  <div class="flow-step__icon">⌕</div>
                  <h4>채용공고 확인</h4>
                  <p>기업별 채용공고 정보를 수집하고 정리해요.</p>
                </div>
                <div class="flow-step"><small>STEP 03</small>
                  <div class="flow-step__icon">▣</div>
                  <h4>지원 일정 관리</h4>
                  <p>전형 상태와 다음 일정을 한눈에 확인해요.</p>
                </div>
                <div class="flow-step"><small>STEP 04</small>
                  <div class="flow-step__icon">☑</div>
                  <h4>할 일 관리</h4>
                  <p>일정별 계획을 세우고 우선순위로 관리해요.</p>
                </div>
                <div class="flow-step"><small>STEP 05</small>
                  <div class="flow-step__icon">↗</div>
                  <h4>성장 기록</h4>
                  <p>학습과 경험을 기록하고 꾸준히 성장해요.</p>
                </div>
              </div>
            </div>

            <div class="card dashboard-preview">
              <h2 class="main-section__title">대시보드 미리보기</h2>

              <%-- [수정] 로그인 사용자에게만 실제 DB 기반 취업 준비 데이터를 표시합니다. --%>
              <c:choose>
                <c:when test="${mainPreviewLoggedIn}">
                  <div class="dashboard-grid">
                    <div class="preview-panel">
                      <h4>오늘의 할 일</h4>
                      <div class="preview-list">
                        <div class="preview-row"><span>오늘 마감 TODO</span><b>${todayTodoCount}건</b></div>
                        <div class="preview-row"><span>진행 중 TODO</span><b>${doingTodoCount}건</b></div>
                        <div class="preview-row"><span>7일 이내 TODO</span><b>${imminentTodoCount}건</b></div>
                      </div>
                    </div>

                    <div class="preview-panel preview-panel--status">
                      <h4>지원 현황 요약</h4>
                      <div class="donut" style="${applicationDonutStyle}">
                        <div class="donut__center">
                          <strong>${applicationTotal}</strong>
                          <span>지원</span>
                        </div>
                      </div>
                      <div class="preview-status-summary" aria-label="지원 상태 요약">
                        <span>관심 ${applicationStatusCounts['INTEREST']}</span>
                        <span>지원 ${applicationStatusCounts['APPLIED']}</span>
                        <span>진행 ${applicationStatusCounts['DOCUMENT'] + applicationStatusCounts['CODING_TEST'] + applicationStatusCounts['INTERVIEW']}</span>
                      </div>
                    </div>

                    <div class="preview-panel">
                      <h4>이번 주 할 일</h4>
                      <div class="preview-list preview-list--tasks">
                        <c:choose>
                          <c:when test="${not empty weekTodos}">
                            <c:forEach var="todo" items="${weekTodos}">
                              <div class="preview-row preview-row--task">
                                <span title="${todo.title}"><c:out value="${todo.title}" /></span>
                                <b>${todo.dueDateLabel}</b>
                              </div>
                            </c:forEach>
                          </c:when>
                          <c:otherwise>
                            <div class="preview-empty">이번 주 예정된 TODO가 없습니다.</div>
                          </c:otherwise>
                        </c:choose>
                      </div>
                    </div>
                  </div>

                  <div class="preview-footer">
                    <c:choose>
                      <c:when test="${not empty recentActivity}">
                        <span class="preview-recent" title="${recentActivity.formattedCreatedAt}">
                          최근 활동 · <c:out value="${recentActivity.title}" /> · ${recentActivity.relativeTime}
                        </span>
                      </c:when>
                      <c:otherwise>
                        <span class="preview-recent">최근 활동 내역이 없습니다.</span>
                      </c:otherwise>
                    </c:choose>
                    <a href="${ctx}/dashboard">전체 보기 →</a>
                  </div>
                </c:when>

                <c:otherwise>
                  <%-- [수정] 비로그인 상태에서는 실제 데이터처럼 보이는 임의 숫자를 노출하지 않습니다. --%>
                  <div class="dashboard-preview-login">
                    <div class="dashboard-preview-login__icon">✓</div>
                    <strong>로그인하면 나의 취업 준비 현황을 확인할 수 있습니다.</strong>
                    <p>TODO, 지원 현황, 최근 활동을 실제 저장된 데이터 기준으로 한눈에 확인해보세요.</p>
                    <a class="jobon-btn jobon-btn--primary" href="${ctx}/login">로그인하고 확인하기</a>
                  </div>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </section>
      </main>

      <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

  </html>