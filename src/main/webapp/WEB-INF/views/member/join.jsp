<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--=========================================================파일
설명=========================================================JOBON 회원가입 JSP입니다. 회원 기본정보/추가정보 입력 필드와 약관 동의, SNS 가입
버튼을 제공하며 join.js와 연결됩니다. --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="ko">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>회원가입 | JOBON</title>
    <link rel="stylesheet" href="${ctx}/css/common.css" />
    <link rel="stylesheet" href="${ctx}/css/member.css" />
  </head>

  <body>
    <c:set var="activeMenu" value="" scope="request" />
    <jsp:include page="/WEB-INF/views/common/header.jsp" />
    <main class="jobon-page">
      <div class="jobon-container">
        <div class="auth-shell">
          <div class="card auth-card auth-card--join">
            <h1 class="auth-title">회원가입</h1>
            <p class="auth-desc">기본 정보와 취업 준비 정보를 입력해주세요.</p>
            <div class="join-steps"><span class="is-active" data-step-indicator="1">1 기본 정보</span><span
                data-step-indicator="2">2 추가 정보</span><span data-step-indicator="3">3 완료</span></div>
            <div id="joinMessage" class="member-message member-message--error" hidden></div>

            <form id="joinForm" class="auth-form" novalidate>
              <section class="join-step" data-step="1">
                <div><label class="form-label" for="loginId">아이디 *</label>
                  <div class="inline-check"><input class="form-control" id="loginId" name="loginId"
                      placeholder="영문/숫자 4~20자"><button class="jobon-btn jobon-btn--outline" type="button"
                      id="checkLoginId">중복 확인</button></div><small class="field-help" id="loginIdHelp"></small>
                </div>
                <div><label class="form-label" for="name">이름 *</label><input class="form-control" id="name"
                    name="name" placeholder="이름을 입력하세요"></div>
                <div><label class="form-label" for="nickname">닉네임 *</label><input class="form-control" id="nickname"
                    name="nickname" placeholder="닉네임을 입력하세요"></div>
                <div><label class="form-label" for="email">이메일 *</label>
                  <div class="inline-check"><input class="form-control" id="email" name="email" type="email"
                      placeholder="example@email.com"><button class="jobon-btn jobon-btn--outline" type="button"
                      id="checkEmail">중복 확인</button></div><small class="field-help" id="emailHelp"></small>
                </div>
                <div><label class="form-label" for="password">비밀번호 *</label><input class="form-control"
                    id="password" name="password" type="password" placeholder="영문·숫자·특수문자 포함 8~20자"></div>
                <div><label class="form-label" for="passwordConfirm">비밀번호 확인 *</label><input class="form-control"
                    id="passwordConfirm" name="passwordConfirm" type="password" placeholder="비밀번호를 다시 입력하세요"></div>
                <div><label class="form-label" for="phone">휴대폰 번호</label><input class="form-control" id="phone"
                    name="phone" placeholder="010-0000-0000"></div>
                <label class="agree-row"><input id="termsAgreed" type="checkbox"> 이용약관에 동의합니다. *</label>
                <label class="agree-row"><input id="privacyAgreed" type="checkbox"> 개인정보 처리방침에 동의합니다. *</label>
                <button class="jobon-btn jobon-btn--primary" type="button" id="nextStep">다음 단계</button>

                <div class="social-divider"><span>SNS 계정으로 간편 가입</span></div>
                <div class="social-row">
                  <a class="social-btn social-btn--google" href="${ctx}/member/google/login">Google</a>
                  <a class="social-btn social-btn--naver" href="${ctx}/member/naver/login">Naver</a>
                  <a class="social-btn social-btn--kakao" href="${ctx}/member/kakao/login">Kakao</a>
                </div>
              </section>

              <section class="join-step" data-step="2" hidden>
                <div><label class="form-label" for="interestJob">관심 직무</label><input class="form-control"
                    id="interestJob" name="interestJob" placeholder="예: 백엔드 개발자"></div>
                <div><label class="form-label" for="careerType">경력 구분</label><select class="form-control"
                    id="careerType" name="careerType">
                    <option value="">선택
                    <option value="NEW">신입</option>
                    <option value="CAREER">경력</option>
                    <option value="INTERN">인턴</option>
                    <option value="ETC">기타</option>
                  </select></div>
                <div><label class="form-label" for="educationLevel">최종 학력</label><select class="form-control"
                    id="educationLevel" name="educationLevel">
                    <option value="">선택</option>
                    <option>고등학교 졸업</option>
                    <option>전문대 졸업</option>
                    <option>대학교 졸업</option>
                    <option>대학원 졸업</option>
                    <option>기타</option>
                  </select></div>
                <div><label class="form-label" for="schoolName">학교명</label><input class="form-control"
                    id="schoolName" name="schoolName" placeholder="학교명을 입력하세요"></div>
                <div><label class="form-label" for="majorName">전공명</label><input class="form-control" id="majorName"
                    name="majorName" placeholder="전공명을 입력하세요"></div>
                <div><label class="form-label" for="preferredLocation">희망 근무지</label><input class="form-control"
                    id="preferredLocation" name="preferredLocation" placeholder="예: 서울"></div>
                <div class="join-actions"><button class="jobon-btn jobon-btn--ghost" type="button" id="prevStep">이전
                    단계</button><button class="jobon-btn jobon-btn--primary" type="submit">가입 완료</button></div>
              </section>
            </form>

            <div class="auth-help auth-center">이미 회원이신가요?&nbsp;<a href="${ctx}/login" class="member-link">로그인</a>
            </div>
          </div>
        </div>
      </div>
    </main>
    <jsp:include page="/WEB-INF/views/common/footer.jsp" />
    <script>window.JOBON_CTX = '${ctx}';</script>
    <script src="${ctx}/js/join.js"></script>
  </body>

</html>