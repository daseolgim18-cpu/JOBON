<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!-- [수정] 스토리보드/ERD 기준 실제 DB 연동 CRUD 및 화면 동작을 적용했습니다. -->
<!doctype html>
<html lang="ko">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>AI 분석 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css?v=20260902-3">
    </head>

    <body>
        <c:set var="activeMenu" value="ai" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>AI 분석</h1>
                        <p>채용공고 원문에서 업무·자격요건·우대기술을 추출하고 내 경험과 비교합니다.</p>
                    </div>
                    <!-- [추가] 저장하기로 보관한 자소서 경험 추천 결과를 다시 확인할 수 있습니다. -->
                    <a class="jobon-btn jobon-btn--soft" href="${ctx}/ai/saved-recommendations">저장한 경험</a>
                </section>
                <c:if test="${not empty successMessage}"><div class="alert alert--success">${successMessage}</div></c:if>
                <c:if test="${not empty errorMessage}"><div class="alert alert--danger">${errorMessage}</div></c:if>
                <form class="card card--padded ai-request" method="post" action="${ctx}/ai/analysis">
                    <label><span class="form-label">분석할 채용공고</span><select class="form-control" id="analysisJobId" name="jobId"
                            required>
                            <option value="">공고 선택</option>
                            <c:forEach var="j" items="${jobs}">
                                <option value="${j.jobId}">${j.companyName} - ${j.title}</option>
                            </c:forEach>
                        </select></label><button class="jobon-btn jobon-btn--primary">분석 요청</button>
                    <div id="analysisQualityGuide" class="alert" style="display:none"></div>
                </form>
                <div class="card table-wrap mt20">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>기업</th>
                                <th>공고</th>
                                <th>상태</th>
                                <th>분석일</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${analyses}">
                                <tr>
                                    <td>${x.companyName}</td>
                                    <td>${x.jobTitle}</td>
                                    <td><span class="badge ${x.status eq 'FAILED' ? 'badge--red' : 'badge--green'}">${x.statusLabel}</span></td>
                                    <td class="date-cell">${x.requestedAtLabel}</td>
                                    <td>
                                        <div class="table-actions table-actions--nowrap">
                                            <a class="text-link" href="${ctx}/ai/analysis/detail?id=${x.analysisId}">상세 보기</a>
                                            <form method="post" action="${ctx}/ai/analysis/${x.analysisId}/delete"
                                                  data-confirm="이 AI 분석 결과를 삭제할까요? 관련 기술 분석과 경험 추천 결과도 함께 삭제됩니다.">
                                                <button class="text-link danger" type="submit">삭제</button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty analyses}">
                                <tr><td colspan="5" class="empty-cell">아직 AI 분석 결과가 없습니다.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
        <script>
        document.addEventListener('DOMContentLoaded', function () {
            const select = document.getElementById('analysisJobId');
            const guide = document.getElementById('analysisQualityGuide');
            if (!select || !guide) return;
            select.addEventListener('change', async function () {
                if (!this.value) { guide.style.display = 'none'; guide.innerHTML = ''; return; }
                try {
                    const response = await fetch('${ctx}/ai/analysis/quality?jobId=' + encodeURIComponent(this.value));
                    if (!response.ok) throw new Error('quality request failed');
                    const warnings = await response.json();
                    if (!warnings.length) {
                        guide.innerHTML = '<strong>분석 준비 완료</strong><br>현재 등록 데이터로 분석을 진행할 수 있습니다.';
                    } else {
                        guide.innerHTML = '<strong>분석 데이터 보완 안내</strong><ul>' + warnings.map(w => '<li>' + w + '</li>').join('') + '</ul>';
                    }
                    guide.style.display = 'block';
                } catch (e) {
                    guide.innerHTML = '데이터 품질 안내를 불러오지 못했습니다. 분석은 계속 진행할 수 있습니다.';
                    guide.style.display = 'block';
                }
            });
        });
        </script>
    </body>

</html>
