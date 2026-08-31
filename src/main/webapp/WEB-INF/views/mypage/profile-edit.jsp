<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>프로필 수정 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
  </head>

  <body>
    <c:set var="activeMenu" value="" scope="request" />
    <jsp:include page="/WEB-INF/views/common/header.jsp" />

    <main class="jobon-page">
      <div class="jobon-container mypage-container">
        <section class="page-heading profile-edit-heading">
          <div>
            <h1>프로필 수정</h1>
            <p>닉네임, 한 줄 소개, 관심 직무와 희망 근무지를 수정하세요.</p>
          </div>
        </section>

        <c:if test="${not empty errorMessage}">
          <div class="mypage-alert mypage-alert--error">${errorMessage}</div>
        </c:if>

        <%-- [수정] 실제 DB 업데이트가 가능하도록 multipart/form-data POST 폼으로 변경했습니다. --%>
          <form class="card card--padded profile-edit-card" action="${ctx}/mypage/profile/edit" method="post"
            enctype="multipart/form-data">

            <div class="profile-edit-top">
              <section class="profile-image-section">
                <h2 class="profile-edit-section-title">프로필 이미지</h2>

                <div class="profile-image-preview" id="profileImagePreview">
                  <c:choose>
                    <c:when test="${not empty member.profileImageUrl}">
                      <c:choose>
                        <c:when
                          test="${fn:startsWith(member.profileImageUrl, 'http://') or fn:startsWith(member.profileImageUrl, 'https://')}">
                          <img src="${member.profileImageUrl}" alt="현재 프로필 이미지" />
                        </c:when>
                        <c:otherwise>
                          <img src="${ctx}${member.profileImageUrl}" alt="현재 프로필 이미지" />
                        </c:otherwise>
                      </c:choose>
                    </c:when>
                    <c:otherwise>
                      <span>${fn:substring(member.name, 0, 1)}</span>
                    </c:otherwise>
                  </c:choose>
                </div>

                <%-- [수정] 브라우저 기본 파일 input을 숨기고 기존 JOBON 버튼 스타일과 어울리게 변경했습니다. --%>
                  <label class="profile-image-btn" for="profileImage">이미지 변경</label>
                  <input class="profile-image-input" id="profileImage" name="profileImage" type="file"
                    accept="image/jpeg,image/png" />
                  <p class="profile-image-help" id="profileImageName">JPG, PNG / 최대 5MB</p>
              </section>

              <section class="profile-basic-section">
                <h2 class="profile-edit-section-title">기본 정보</h2>
                <div class="form-group">
                  <label class="form-label" for="nickname">닉네임</label>

                  <%-- [수정] 닉네임 입력창 오른쪽에 중복 확인 버튼을 배치합니다. --%>
                    <div class="profile-nickname-row">
                      <%-- [수정] 현재 DB 값을 value로 출력합니다. --%>
                        <input class="form-control profile-form-control" id="nickname" name="nickname" type="text"
                          maxlength="50" value="${fn:escapeXml(member.nickname)}"
                          data-original-nickname="${fn:escapeXml(member.nickname)}" placeholder="닉네임을 입력하세요"
                          required />

                        <button class="profile-nickname-check-btn" id="nicknameCheckBtn" type="button">
                          중복 확인
                        </button>
                    </div>

                    <%-- [수정] 닉네임 중복 확인 결과를 입력창 아래에 표시합니다. --%>
                      <p class="profile-nickname-message" id="nicknameCheckMessage"></p>
                </div>
              </section>
            </div>

            <div class="profile-edit-divider"></div>

            <section>
              <h2 class="profile-edit-section-title">프로필 정보</h2>
              <div class="form-grid profile-edit-grid">
                <div class="form-group form-group--full">
                  <label class="form-label" for="introduction">한 줄 소개</label>
                  <input class="form-control profile-form-control" id="introduction" name="introduction" type="text"
                    maxlength="300" value="${fn:escapeXml(member.introduction)}" placeholder="나를 간단하게 소개해 주세요." />
                </div>

                <div class="form-group">
                  <label class="form-label" for="interestJob">관심 직무</label>
                  <%-- [수정] 회원가입에서 자유 입력으로 저장하므로 수정 화면도 자유 입력 방식으로 맞췄습니다. --%>
                    <input class="form-control profile-form-control" id="interestJob" name="interestJob" type="text"
                      maxlength="100" value="${fn:escapeXml(member.interestJob)}" placeholder="예: 백엔드 개발자" />
                </div>

                <div class="form-group">
                  <label class="form-label" for="preferredLocation">희망 근무지</label>
                  <input class="form-control profile-form-control" id="preferredLocation" name="preferredLocation"
                    type="text" maxlength="100" value="${fn:escapeXml(member.preferredLocation)}"
                    placeholder="예: 서울 / 경기" />
                </div>
              </div>
            </section>

            <div class="form-actions profile-edit-actions">
              <a class="jobon-btn jobon-btn--ghost" href="${ctx}/mypage">취소</a>
              <button class="jobon-btn jobon-btn--primary" type="submit">저장하기</button>
            </div>
          </form>
      </div>
    </main>

    <jsp:include page="/WEB-INF/views/common/footer.jsp" />

    <script src="${ctx}/js/mypage/profile-edit.js"></script>
  </body>

</html>