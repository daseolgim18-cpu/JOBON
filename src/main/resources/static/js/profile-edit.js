/**
 * =========================================================
 * 파일 설명
 * =========================================================
 * 마이페이지 프로필 수정 화면 전용 JavaScript입니다.
 * 프로필 이미지 미리보기와 닉네임 중복 확인 기능을 처리합니다.
 */

document.addEventListener("DOMContentLoaded", function () {
  const profileEditForm = document.querySelector(".profile-edit-card");

  if (!profileEditForm) {
    return;
  }

  const contextPath = profileEditForm.dataset.contextPath || "";

  /**
   * =========================================================
   * 프로필 이미지 미리보기
   * =========================================================
   */

  const profileImageInput = document.getElementById("profileImage");
  const profileImagePreview = document.getElementById("profileImagePreview");
  const profileImageName = document.getElementById("profileImageName");

  if (profileImageInput) {
    profileImageInput.addEventListener("change", function () {
      const file = this.files && this.files[0];

      if (!file) {
        profileImageName.textContent = "JPG, PNG / 최대 5MB";
        return;
      }

      if (!["image/jpeg", "image/png"].includes(file.type)) {
        alert("JPG 또는 PNG 파일만 선택할 수 있습니다.");

        this.value = "";
        profileImageName.textContent = "JPG, PNG / 최대 5MB";

        return;
      }

      if (file.size > 5 * 1024 * 1024) {
        alert("프로필 이미지는 5MB 이하만 선택할 수 있습니다.");

        this.value = "";
        profileImageName.textContent = "JPG, PNG / 최대 5MB";

        return;
      }

      const reader = new FileReader();

      reader.onload = function (event) {
        profileImagePreview.innerHTML = '<img src="' + event.target.result + '" alt="선택한 프로필 이미지 미리보기">';
      };

      reader.readAsDataURL(file);

      profileImageName.textContent = file.name;
    });
  }

  /**
   * =========================================================
   * [수정] 닉네임 중복 확인
   * =========================================================
   */

  const nicknameInput = document.getElementById("nickname");
  const nicknameCheckBtn = document.getElementById("nicknameCheckBtn");
  const nicknameCheckMessage = document.getElementById("nicknameCheckMessage");

  if (!nicknameInput || !nicknameCheckBtn || !nicknameCheckMessage) {
    return;
  }

  const originalNickname = (nicknameInput.dataset.originalNickname || "").trim();

  // [수정] 기존 닉네임을 그대로 사용하는 경우
  // 별도의 중복 확인 없이 저장할 수 있습니다.
  let nicknameChecked = true;

  // [수정] 닉네임이 변경되면 기존 중복 확인 결과를 초기화합니다.
  nicknameInput.addEventListener("input", function () {
    const nickname = this.value.trim();

    nicknameCheckMessage.textContent = "";
    nicknameCheckMessage.className = "profile-nickname-message";

    nicknameChecked = nickname === originalNickname;
  });

  // [수정] 중복 확인 버튼 클릭 시
  // 현재 로그인 회원을 제외한 닉네임 중복 여부를 확인합니다.
  nicknameCheckBtn.addEventListener("click", async function () {
    const nickname = nicknameInput.value.trim();

    if (!nickname) {
      nicknameChecked = false;

      nicknameCheckMessage.textContent = "닉네임을 입력해주세요.";
      nicknameCheckMessage.className = "profile-nickname-message is-error";

      nicknameInput.focus();

      return;
    }

    if (nickname.length > 50) {
      nicknameChecked = false;

      nicknameCheckMessage.textContent = "닉네임은 50자 이하로 입력해주세요.";

      nicknameCheckMessage.className = "profile-nickname-message is-error";

      nicknameInput.focus();

      return;
    }

    // [수정] 기존에 사용 중인 닉네임은
    // 별도의 DB 중복 확인 없이 그대로 사용할 수 있습니다.
    if (nickname === originalNickname) {
      nicknameChecked = true;

      nicknameCheckMessage.textContent = "현재 사용 중인 닉네임입니다.";

      nicknameCheckMessage.className = "profile-nickname-message is-success";

      return;
    }

    try {
      nicknameCheckBtn.disabled = true;
      nicknameCheckBtn.textContent = "확인 중";

      const response = await fetch(contextPath + "/api/members/check-nickname?nickname=" + encodeURIComponent(nickname));

      if (!response.ok) {
        throw new Error("닉네임 중복 확인 요청 실패");
      }

      const data = await response.json();

      if (data.available) {
        nicknameChecked = true;

        nicknameCheckMessage.textContent = data.message || "사용 가능한 닉네임입니다.";

        nicknameCheckMessage.className = "profile-nickname-message is-success";
      } else {
        nicknameChecked = false;

        nicknameCheckMessage.textContent = data.message || "이미 사용 중인 닉네임입니다.";

        nicknameCheckMessage.className = "profile-nickname-message is-error";
      }
    } catch (error) {
      nicknameChecked = false;

      nicknameCheckMessage.textContent = "중복 확인 중 오류가 발생했습니다.";

      nicknameCheckMessage.className = "profile-nickname-message is-error";
    } finally {
      nicknameCheckBtn.disabled = false;
      nicknameCheckBtn.textContent = "중복 확인";
    }
  });

  // [수정] 닉네임을 변경한 뒤 중복 확인하지 않은 경우
  // 프로필 저장을 차단합니다.
  profileEditForm.addEventListener("submit", function (event) {
    const nickname = nicknameInput.value.trim();

    if (nickname !== originalNickname && !nicknameChecked) {
      event.preventDefault();

      nicknameCheckMessage.textContent = "닉네임 중복 확인을 해주세요.";

      nicknameCheckMessage.className = "profile-nickname-message is-error";

      nicknameInput.focus();
    }
  });
});
