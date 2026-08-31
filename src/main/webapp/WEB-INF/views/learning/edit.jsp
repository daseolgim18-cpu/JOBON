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
        <title>성장 기록 수정 | JOBON</title>
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
                        <h1>성장 기록 수정</h1>
                    </div>
                </section>
                <form class="card card--padded" method="post" action="${ctx}/learning/${record.learningId}">
                    <div class="form-grid"><label><span class="form-label">구분</span><select class="form-control"
                                name="recordType">
                                <option value="LEARNING">학습</option>
                                <option value="CERTIFICATE">자격증</option>
                                <option value="ACTIVITY">활동</option>
                            </select></label><label><span class="form-label">학습일</span><input
                                class="form-control" type="date" name="learningDate"
                                value="${record.learningDate}"></label><label class="form-group--full"><span
                                class="form-label">주제 *</span><input class="form-control" name="subject"
                                value="${record.subject}" required></label><label class="form-group--full"><span
                                class="form-label">기술 키워드</span><input class="form-control" name="techNames"
                                value="${record.techNames}"
                                placeholder="Java, Spring Boot, Oracle"></label><label
                            class="form-group--full"><span class="form-label">학습 내용</span><textarea
                                class="form-control tall"
                                name="content">${record.content}</textarea></label><label
                            class="form-group--full"><span class="form-label">어려웠던 점</span><textarea
                                class="form-control"
                                name="difficulty">${record.difficulty}</textarea></label><label
                            class="form-group--full"><span class="form-label">느낀 점 / 활용 계획</span><textarea
                                class="form-control" name="reflection">${record.reflection}</textarea></label>
                    </div>
                    <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                            href="${ctx}/learning/detail?id=${record.learningId}">취소</a><button
                            class="jobon-btn jobon-btn--primary">저장</button></div>
                </form>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
        <script src="${ctx}/js/jobon-crud.js"></script>
    </body>

</html>