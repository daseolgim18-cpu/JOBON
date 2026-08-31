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
        <title>프로젝트 경험 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css">
        <link rel="stylesheet" href="${ctx}/css/domain.css">
    </head>

    <body>
        <c:set var="activeMenu" value="project" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>프로젝트 경험</h1>
                        <p>기술·담당 기능·트러블슈팅까지 자소서에 활용할 수 있도록 관리합니다.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/project/new">프로젝트 등록</a>
                </section>
                <div class="card table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>프로젝트</th>
                                <th>기간</th>
                                <th>역할</th>
                                <th>기술</th>
                                <th>관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="x" items="${projects}">
                                <tr>
                                    <td>${x.projectName}</td>
                                    <td>${x.startDate} ~ ${x.endDate}</td>
                                    <td>${x.roleName}</td>
                                    <td>${x.techNames}</td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/project/detail?id=${x.projectId}">상세</a><a
                                                class="text-link"
                                                href="${ctx}/project/edit?id=${x.projectId}">수정</a>
                                            <form method="post" action="${ctx}/project/${x.projectId}/delete"
                                                data-confirm="프로젝트를 삭제할까요?"><button
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