<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>통합 검색 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css" />

    </head>

    <body>
        <c:set var="activeMenu" value="" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>통합 검색</h1>
                        <p>기업명, 공고명 또는 키워드로 검색한 결과입니다.</p>
                    </div>
                </section>
                <div class="toolbar"><input class="form-control toolbar__grow" value="${param.keyword}"
                        placeholder="검색어"><button class="jobon-btn jobon-btn--primary">검색</button></div>
                <div class="card empty-panel">
                    <div class="empty-panel__icon">⌕</div><strong>검색 결과 영역</strong>
                    <p>기업 및 채용공고 검색 결과를 연결할 화면 틀입니다.</p>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>