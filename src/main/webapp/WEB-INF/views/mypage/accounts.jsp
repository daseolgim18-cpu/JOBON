<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>연동 계정 관리 | JOBON</title>
        <link rel="stylesheet" href="${ctx}/css/common.css" />

    </head>

    <body>
        <c:set var="activeMenu" value="" scope="request" />
        <jsp:include page="/WEB-INF/views/common/header.jsp" />
        <main class="jobon-page">
            <div class="jobon-container">
                <section class="page-heading">
                    <div>
                        <h1>연동 계정 관리</h1>
                        <p>Google, Naver, Kakao 계정의 연동 상태를 관리하세요.</p>
                    </div>
                </section>
                <div class="mypage-layout">
                    <aside class="card side-nav"><a href="${ctx}/mypage">내 프로필</a><%-- [수정] 일반 회원만 비밀번호를 보유하므로 SNS
                            전용 회원에게는 비밀번호 변경 메뉴를 표시하지 않습니다. --%>
                            <c:if test="${not empty member.passwordHash}"><a href="${ctx}/mypage/password">비밀번호
                                    변경</a></c:if><a class="is-active" href="${ctx}/mypage/accounts">연동 계정</a><a
                                href="${ctx}/mypage/activity">활동 내역</a><a href="${ctx}/mypage/searches">저장된 검색어</a>
                    </aside>
                    <section class="card card--padded">

                        <%-- [추가] 연동/해제 처리 결과를 같은 화면에서 확인할 수 있도록 안내 메시지를 표시합니다. --%>
                            <c:if test="${not empty successMessage}">
                                <div class="mypage-alert mypage-alert--success">${successMessage}</div>
                            </c:if>
                            <c:if test="${not empty errorMessage}">
                                <div class="mypage-alert mypage-alert--error">${errorMessage}</div>
                            </c:if>

                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>서비스</th>
                                        <th>상태</th>
                                        <th>이메일</th>
                                        <th>관리</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%-- [수정] 기존 sample@gmail.com 하드코딩을 제거하고 SOCIAL_ACCOUNT의 실제 Google 연동 정보를 표시합니다.
                                        --%>
                                        <tr>
                                            <td>Google</td>
                                            <c:choose>
                                                <c:when test="${not empty googleAccount}">
                                                    <td><span class="badge badge--green">연동</span></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${not empty googleAccount.email}">
                                                                ${googleAccount.email}</c:when>
                                                            <c:otherwise>-</c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td>
                                                        <form method="post"
                                                            action="${ctx}/mypage/accounts/google/unlink"
                                                            style="display:inline"
                                                            onsubmit="return confirm('Google 계정 연동을 해제하시겠습니까?');">
                                                            <button type="submit" class="text-link danger">연동
                                                                해제</button>
                                                        </form>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td><span class="badge badge--blue">미연동</span></td>
                                                    <td>-</td>
                                                    <td><a class="text-link"
                                                            href="${ctx}/mypage/accounts/google/link">연동하기</a></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>

                                        <%-- [수정] Naver의 실제 연동 여부와 이메일에 따라 연동하기/연동 해제 UI를 전환합니다. --%>
                                            <tr>
                                                <td>Naver</td>
                                                <c:choose>
                                                    <c:when test="${not empty naverAccount}">
                                                        <td><span class="badge badge--green">연동</span></td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${not empty naverAccount.email}">
                                                                    ${naverAccount.email}</c:when>
                                                                <c:otherwise>-</c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <form method="post"
                                                                action="${ctx}/mypage/accounts/naver/unlink"
                                                                style="display:inline"
                                                                onsubmit="return confirm('Naver 계정 연동을 해제하시겠습니까?');">
                                                                <button type="submit" class="text-link danger">연동
                                                                    해제</button>
                                                            </form>
                                                        </td>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <td><span class="badge badge--blue">미연동</span></td>
                                                        <td>-</td>
                                                        <td><a class="text-link"
                                                                href="${ctx}/mypage/accounts/naver/link">연동하기</a>
                                                        </td>
                                                    </c:otherwise>
                                                </c:choose>
                                            </tr>

                                            <%-- [수정] Kakao의 실제 연동 여부와 이메일에 따라 연동하기/연동 해제 UI를 전환합니다. --%>
                                                <tr>
                                                    <td>Kakao</td>
                                                    <c:choose>
                                                        <c:when test="${not empty kakaoAccount}">
                                                            <td><span class="badge badge--green">연동</span></td>
                                                            <td>
                                                                <c:choose>
                                                                    <c:when test="${not empty kakaoAccount.email}">
                                                                        ${kakaoAccount.email}</c:when>
                                                                    <c:otherwise>-</c:otherwise>
                                                                </c:choose>
                                                            </td>
                                                            <td>
                                                                <form method="post"
                                                                    action="${ctx}/mypage/accounts/kakao/unlink"
                                                                    style="display:inline"
                                                                    onsubmit="return confirm('Kakao 계정 연동을 해제하시겠습니까?');">
                                                                    <button type="submit"
                                                                        class="text-link danger">연동 해제</button>
                                                                </form>
                                                            </td>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <td><span class="badge badge--blue">미연동</span></td>
                                                            <td>-</td>
                                                            <td><a class="text-link"
                                                                    href="${ctx}/mypage/accounts/kakao/link">연동하기</a>
                                                            </td>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </tr>
                                </tbody>
                            </table>

                            <%-- [추가] 비밀번호가 없는 SNS 전용 회원에게 마지막 SNS 연동은 해제할 수 없다는 규칙을 안내합니다. --%>
                                <c:if test="${empty member.passwordHash}">
                                    <p class="muted" style="margin-top:16px;">SNS 전용 회원은 로그인 가능한 계정이 하나 이상 필요합니다.
                                        마지막으로 남은 SNS 계정은 해제할 수 없습니다.</p>
                                </c:if>
                    </section>
                </div>
            </div>
        </main>
        <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    </body>

</html>