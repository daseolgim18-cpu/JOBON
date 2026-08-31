<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">
  <head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1"/>
  <title>마이페이지 | JOBON</title>
  <link rel="stylesheet" href="${ctx}/css/common.css"/>
  </head>
  <body>
  <c:set var="activeMenu" value="" scope="request"/>
  <jsp:include page="/WEB-INF/views/common/header.jsp"/>

  <main class="jobon-page">
    <div class="jobon-container mypage-container">
      <div class="mypage-layout">
        <aside class="card side-nav">
          <a class="is-active" href="${ctx}/mypage">내 프로필</a>

          <%-- [수정] 일반 회원은 비밀번호가 존재하므로 비밀번호 변경 메뉴를 표시하고,
              SNS 전용 회원은 PASSWORD_HASH가 null이므로 메뉴를 표시하지 않습니다. --%>
          <c:if test="${not empty member.passwordHash}">
            <a href="${ctx}/mypage/password">비밀번호 변경</a>
          </c:if>

          <a href="${ctx}/mypage/accounts">연동 계정</a>
          <a href="${ctx}/mypage/activity">활동 내역</a>
          <a href="${ctx}/mypage/searches">저장된 검색어</a>
        </aside>

        <section class="mypage-content">
          <section class="page-heading">
            <div>
              <h1>마이페이지</h1>
              <p>프로필과 JOBON 활동 정보를 관리하세요.</p>
            </div>
            <a class="jobon-btn jobon-btn--primary" href="${ctx}/mypage/profile/edit">프로필 수정</a>
          </section>

          <c:if test="${not empty successMessage}">
            <div class="mypage-alert mypage-alert--success">${successMessage}</div>
          </c:if>

          <div class="card card--padded profile-card">
            <div class="profile-summary">
              <div class="profile-avatar">
                <%-- [수정] SNS 프로필 또는 직접 업로드한 이미지가 있으면 DB 값을 사용합니다. --%>
                <c:choose>
                  <c:when test="${not empty member.profileImageUrl}">
                    <c:choose>
                      <c:when test="${fn:startsWith(member.profileImageUrl, 'http://') or fn:startsWith(member.profileImageUrl, 'https://')}">
                        <img src="${member.profileImageUrl}" alt="${member.name} 프로필 이미지"/>
                      </c:when>
                      <c:otherwise>
                        <img src="${ctx}${member.profileImageUrl}" alt="${member.name} 프로필 이미지"/>
                      </c:otherwise>
                    </c:choose>
                  </c:when>
                  <c:otherwise>
                    <span>${fn:substring(member.name, 0, 1)}</span>
                  </c:otherwise>
                </c:choose>
              </div>

              <div class="profile-summary__text">
                <div class="profile-name-row">
                  <%-- [수정] 하드코딩 대신 JOBON_MEMBER의 NAME을 출력합니다. --%>
                  <h2>${member.name}</h2>
                  <c:choose>
                    <c:when test="${not empty sessionScope.loginProvider}">
                      <span class="profile-login-badge">${sessionScope.loginProvider} 로그인</span>
                    </c:when>
                    <c:otherwise>
                      <span class="profile-login-badge profile-login-badge--normal">일반 회원</span>
                    </c:otherwise>
                  </c:choose>
                </div>

                <%-- [수정] SNS 회원 등 소개가 비어 있는 경우 화면에 null 대신 안내 문구를 표시합니다. --%>
                <p class="muted profile-introduction">
                  <c:choose>
                    <c:when test="${not empty member.introduction}">${member.introduction}</c:when>
                    <c:otherwise>프로필을 수정하여 나만의 취업 준비 정보를 채워보세요.</c:otherwise>
                  </c:choose>
                </p>
              </div>
            </div>

            <%-- [수정] 모든 정보는 로그인 회원의 JOBON_MEMBER DB 조회 결과를 사용합니다. --%>
            <dl class="detail-list profile-detail-list">
              <div>
                <dt>닉네임</dt>
                <dd>${empty member.nickname ? '미설정' : member.nickname}</dd>
              </div>
              <div>
                <dt>관심 직무</dt>
                <dd>${empty member.interestJob ? '미설정' : member.interestJob}</dd>
              </div>
              <div>
                <dt>경력 구분</dt>
                <dd>
                  <c:choose>
                    <c:when test="${member.careerType eq 'NEW'}">신입</c:when>
                    <c:when test="${member.careerType eq 'CAREER'}">경력</c:when>
                    <c:when test="${member.careerType eq 'INTERN'}">인턴</c:when>
                    <c:when test="${member.careerType eq 'ETC'}">기타</c:when>
                    <c:otherwise>미설정</c:otherwise>
                  </c:choose>
                </dd>
              </div>
              <div>
                <dt>희망 근무지</dt>
                <dd>${empty member.preferredLocation ? '미설정' : member.preferredLocation}</dd>
              </div>
              <div>
                <dt>이메일</dt>
                <dd>
                  <%-- SNS에서 이메일 제공 동의를 받지 못해 생성된 내부용 이메일은 사용자에게 그대로 노출하지 않습니다. --%>
                  <c:choose>
                    <c:when test="${fn:endsWith(member.email, '@social.jobon.local')}">SNS 제공 정보 없음</c:when>
                    <c:otherwise>${member.email}</c:otherwise>
                  </c:choose>
                </dd>
              </div>
            </dl>
          </div>
        </section>
      </div>
    </div>
  </main>

  <jsp:include page="/WEB-INF/views/common/footer.jsp"/>
  </body>
</html>
