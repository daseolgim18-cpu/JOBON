<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>활동 내역 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css" />
        <link rel="stylesheet" href="${ctx}/css/domain.css" />
        <link rel="stylesheet" href="${ctx}/css/main.css" />
    </head>

    <body>
        <c:set var="activeMenu" value="" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>활동 내역</h1>
                        <p>JOBON에서 수행한 주요 활동을 시간순으로 확인하세요.</p>
                    </div>
                </section>

                <div class="mypage-layout">
                    <aside class="card side-nav">
                        <a href="${ctx}/mypage">내 프로필</a>
                        <a href="${ctx}/mypage/password">비밀번호 변경</a>
                        <a href="${ctx}/mypage/accounts">연동 계정</a>
                        <a href="${ctx}/mypage/activity" class="is-active">활동 내역</a>
                        <a href="${ctx}/mypage/searches">저장된 검색어</a><a href="${ctx}/mypage/withdraw">회원 탈퇴</a>
                    </aside>

                    <section class="card card--padded">
                        <!-- [수정] 샘플 select를 실제 활동 유형 필터와 연동합니다. -->
                        <form class="toolbar" method="get" action="${ctx}/mypage/activity">
                            <select class="form-control" name="type" style="max-width:190px" onchange="this.form.submit()">
                                <option value="" ${empty type ? 'selected' : ''}>전체 활동</option>
                                <option value="COMPANY" ${type == 'COMPANY' ? 'selected' : ''}>기업</option>
                                <option value="JOB" ${type == 'JOB' ? 'selected' : ''}>채용공고</option>
                                <option value="APPLICATION" ${type == 'APPLICATION' ? 'selected' : ''}>지원 현황</option>
                                <option value="TODO" ${type == 'TODO' ? 'selected' : ''}>TODO</option>
                                <option value="LEARNING" ${type == 'LEARNING' ? 'selected' : ''}>성장 기록</option>
                                <option value="PROJECT" ${type == 'PROJECT' ? 'selected' : ''}>프로젝트</option>
                                <option value="AI" ${type == 'AI' ? 'selected' : ''}>AI 분석</option>
                            </select>
                        </form>

                        <!-- [수정] 기존 하드코딩 샘플을 ACTIVITY_LOG 실제 조회 결과로 교체합니다. -->
                        <div class="preview-list">
                            <c:choose>
                                <c:when test="${empty activities}">
                                    <div class="empty-panel">
                                        <div class="empty-panel__icon">✓</div>
                                        <strong>아직 기록된 활동이 없습니다.</strong>
                                        <p>기업, 채용공고, 지원 현황, TODO, 성장 기록, 프로젝트를 관리하거나 AI 분석을 실행하면 이곳에 기록됩니다.</p>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="activity" items="${activities}">
                                        <div class="preview-row">
                                            <span>
                                                <strong><c:out value="${activity.activityTypeLabel}" /></strong>
                                                · <c:out value="${activity.title}" />
                                                <small style="margin-left:8px; color:#7a8494;"><c:out value="${activity.actionTypeLabel}" /></small>
                                            </span>
                                            <b title="${activity.formattedCreatedAt}"><c:out value="${activity.relativeTime}" /></b>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </section>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>
