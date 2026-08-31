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
        <title>성장 기록 상세 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="learning" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>${record.subject}</h1>
                        <p>${record.recordType} · ${record.learningDate}</p>
                    </div>
                    <div class="action-row"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/learning/edit?id=${record.learningId}">수정</a>
                        <form method="post" action="${ctx}/learning/${record.learningId}/delete"
                            data-confirm="성장 기록을 삭제할까요?"><button class="jobon-btn jobon-btn--danger">삭제</button>
                        </form>
                    </div>
                </section>
                <div class="card card--padded">
                    <h3>기술 키워드</h3>
                    <div class="tag-cloud">
                        <c:forTokens items="${record.techNames}" delims="," var="t"><span
                                class="skill-tag">${t}</span></c:forTokens>
                    </div>
                    <h3>학습 내용</h3>
                    <p class="preline">${record.content}</p>
                    <h3>어려웠던 점</h3>
                    <p class="preline">${record.difficulty}</p>
                    <h3>느낀 점 / 활용 계획</h3>
                    <p class="preline">${record.reflection}</p>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>