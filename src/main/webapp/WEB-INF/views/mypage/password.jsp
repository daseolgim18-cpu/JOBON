<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>비밀번호 변경 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css" />

    </head>

    <body>
        <c:set var="activeMenu" value="" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>비밀번호 변경</h1>
                        <p>안전한 계정 관리를 위해 비밀번호를 변경하세요.</p>
                    </div>
                </section>
                <div class="mypage-layout">
                    <aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a
                            href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a
                            href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a>
                    </aside>
                    <section class="card card--padded">
                        <div class="auth-form">
                            <div><label class="form-label">현재 비밀번호</label><input class="form-control"
                                    type="password"></div>
                            <div><label class="form-label">새 비밀번호</label><input class="form-control"
                                    type="password"></div>
                            <div><label class="form-label">새 비밀번호 확인</label><input class="form-control"
                                    type="password"></div>
                            <div class="form-actions"><a class="jobon-btn jobon-btn--ghost"
                                    href="${ctx}/mypage">취소</a><button
                                    class="jobon-btn jobon-btn--primary">변경하기</button></div>
                        </div>
                    </section>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>