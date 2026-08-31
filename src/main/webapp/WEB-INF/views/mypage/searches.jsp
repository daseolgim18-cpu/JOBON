<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>저장된 검색어 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css" />

    </head>

    <body>
        <c:set var="activeMenu" value="" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>저장된 검색어</h1>
                        <p>자주 사용하는 검색 조건을 저장하고 다시 실행하세요.</p>
                    </div><a class="jobon-btn jobon-btn--primary" href="${ctx}/mypage/searches/new">새 검색어 저장</a>
                </section>
                <div class="mypage-layout">
                    <aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><a
                            href="${ctx}/mypage/password">비밀번호 변경</a><a href="${ctx}/mypage/accounts">연동 계정</a><a
                            href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a>
                    </aside>
                    <section class="card table-wrap">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>검색 이름</th>
                                    <th>대상</th>
                                    <th>조건</th>
                                    <th>저장일</th>
                                    <th>관리</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td>백엔드 신입 서울</td>
                                    <td>채용공고</td>
                                    <td>백엔드 · 신입 · 서울</td>
                                    <td>2026.08.26</td>
                                    <td>
                                        <div class="table-actions"><a class="text-link"
                                                href="${ctx}/job/list">검색</a><button
                                                class="text-link danger">삭제</button></div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </section>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>