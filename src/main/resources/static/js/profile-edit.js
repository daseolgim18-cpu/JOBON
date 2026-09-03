/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 마이페이지 프로필 수정 화면 전용 JavaScript입니다.
 * 프로필 이미지 미리보기와 닉네임/이메일 중복 확인 기능을 처리합니다.
 */
document.addEventListener("DOMContentLoaded", function () {
  const profileEditForm = document.querySelector(".profile-edit-card");
  if (!profileEditForm) return;
  const contextPath = profileEditForm.dataset.contextPath || "";

  const profileImageInput = document.getElementById("profileImage");
  const profileImagePreview = document.getElementById("profileImagePreview");
  const profileImageName = document.getElementById("profileImageName");
  if (profileImageInput) {
    profileImageInput.addEventListener("change", function () {
      const file = this.files && this.files[0];
      if (!file) { profileImageName.textContent = "JPG, PNG / 최대 5MB"; return; }
      if (!["image/jpeg", "image/png"].includes(file.type)) {
        alert("JPG 또는 PNG 파일만 선택할 수 있습니다."); this.value = "";
        profileImageName.textContent = "JPG, PNG / 최대 5MB"; return;
      }
      if (file.size > 5 * 1024 * 1024) {
        alert("프로필 이미지는 5MB 이하만 선택할 수 있습니다."); this.value = "";
        profileImageName.textContent = "JPG, PNG / 최대 5MB"; return;
      }
      const reader = new FileReader();
      reader.onload = function (event) {
        profileImagePreview.innerHTML = '<img src="' + event.target.result + '" alt="선택한 프로필 이미지 미리보기">';
      };
      reader.readAsDataURL(file);
      profileImageName.textContent = file.name;
    });
  }

  function bindDuplicateCheck(options) {
    const input = document.getElementById(options.inputId);
    const button = document.getElementById(options.buttonId);
    const message = document.getElementById(options.messageId);
    if (!input || !button || !message) return () => true;

    const original = (input.dataset[options.originalDataKey] || "").trim().toLowerCase();
    let checked = true;
    input.addEventListener("input", function () {
      message.textContent = "";
      message.className = "profile-nickname-message";
      checked = this.value.trim().toLowerCase() === original;
    });

    button.addEventListener("click", async function () {
      const value = input.value.trim();
      if (!value || !options.validate(value)) {
        checked = false;
        message.textContent = options.invalidMessage;
        message.className = "profile-nickname-message is-error";
        input.focus(); return;
      }
      if (value.toLowerCase() === original) {
        checked = true;
        message.textContent = options.currentMessage;
        message.className = "profile-nickname-message is-success";
        return;
      }
      try {
        button.disabled = true; button.textContent = "확인 중";
        const response = await fetch(contextPath + options.url + encodeURIComponent(value));
        if (!response.ok) throw new Error("duplicate check failed");
        const data = await response.json();
        checked = !!data.available;
        message.textContent = data.message || (checked ? options.successMessage : options.duplicateMessage);
        message.className = "profile-nickname-message " + (checked ? "is-success" : "is-error");
      } catch (error) {
        checked = false;
        message.textContent = "중복 확인 중 오류가 발생했습니다.";
        message.className = "profile-nickname-message is-error";
      } finally {
        button.disabled = false; button.textContent = "중복 확인";
      }
    });
    return function () {
      const changed = input.value.trim().toLowerCase() !== original;
      if (changed && !checked) {
        message.textContent = options.needCheckMessage;
        message.className = "profile-nickname-message is-error";
        input.focus(); return false;
      }
      return true;
    };
  }

  const validateNickname = bindDuplicateCheck({
    inputId: "nickname", buttonId: "nicknameCheckBtn", messageId: "nicknameCheckMessage",
    originalDataKey: "originalNickname", url: "/api/members/check-nickname?nickname=",
    validate: value => value.length <= 50, invalidMessage: "닉네임을 1~50자로 입력해주세요.",
    currentMessage: "현재 사용 중인 닉네임입니다.", successMessage: "사용 가능한 닉네임입니다.",
    duplicateMessage: "이미 사용 중인 닉네임입니다.", needCheckMessage: "닉네임 중복 확인을 해주세요."
  });

  const validateEmail = bindDuplicateCheck({
    inputId: "email", buttonId: "emailCheckBtn", messageId: "emailCheckMessage",
    originalDataKey: "originalEmail", url: "/api/members/check-profile-email?email=",
    validate: value => /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(value) && value.length <= 150,
    invalidMessage: "올바른 이메일 주소를 입력해주세요.", currentMessage: "현재 사용 중인 이메일입니다.",
    successMessage: "사용 가능한 이메일입니다.", duplicateMessage: "이미 사용 중인 이메일입니다.",
    needCheckMessage: "이메일 중복 확인을 해주세요."
  });

  profileEditForm.addEventListener("submit", function (event) {
    if (!validateNickname() || !validateEmail()) event.preventDefault();
  });
});
