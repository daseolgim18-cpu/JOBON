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
        <title>성장 기록 | JOBON</title>
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
                        <h1>성장 기록</h1>
                        <p>학습 내용과 기술 키워드를 프로젝트/공고 분석에 연결합니다.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/learning/new">기록 등록</a>
                </section>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>구분</th>
                                <th>주제</th>
                                <th>기술</th>
                                <th>학습일</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${records}">
                                <tr>
                                    <td>${x.recordType}</td>
                                    <td>${x.subject}</td>
                                    <td>${x.techNames}</td>
                                    <td>${x.learningDate}</td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/learning/detail?id=${x.learningId}">상세</a><a
                                                class="text-link"
                                                href="${ctx}/learning/edit?id=${x.learningId}">수정</a>
                                            <form method="post" action="${ctx}/learning/${x.learningId}/delete"
                                                data-confirm="성장 기록을 삭제할까요?"><button
                                                    class="text-link danger">삭제</button></form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>