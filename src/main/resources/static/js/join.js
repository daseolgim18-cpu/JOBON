/*
=========================================================
파일 설명
=========================================================
회원가입 화면 JavaScript입니다.
입력값 1차 검증, 아이디/이메일 중복확인 API 호출, 최종 회원가입 API 요청을 담당합니다.
*/

(() => {
  const ctx = window.JOBON_CTX || "";
  const form = document.getElementById("joinForm");
  const msg = document.getElementById("joinMessage");
  let loginIdChecked = false,
    emailChecked = false;

  document.getElementById("loginId").addEventListener("input", () => (loginIdChecked = false));
  document.getElementById("email").addEventListener("input", () => (emailChecked = false));

  document.getElementById("checkLoginId").addEventListener("click", async () => {
    const v = val("loginId");
    if (!/^[a-zA-Z0-9_]{4,20}$/.test(v)) return help("loginIdHelp", "아이디는 영문, 숫자, 밑줄 4~20자로 입력해주세요.", false);
    const d = await get("/api/members/check-login-id?loginId=" + encodeURIComponent(v));
    loginIdChecked = !!d.available;
    help("loginIdHelp", d.available ? "사용 가능한 아이디입니다." : "이미 사용 중인 아이디입니다.", d.available);
  });
  document.getElementById("checkEmail").addEventListener("click", async () => {
    const v = val("email");
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(v)) return help("emailHelp", "올바른 이메일을 입력해주세요.", false);
    const d = await get("/api/members/check-email?email=" + encodeURIComponent(v));
    emailChecked = !!d.available;
    help("emailHelp", d.available ? "사용 가능한 이메일입니다." : "이미 사용 중인 이메일입니다.", d.available);
  });
  document.getElementById("nextStep").addEventListener("click", () => {
    const error = validateBasic();
    if (error) return show(error);
    show("");
    document.querySelector('[data-step="1"]').hidden = true;
    document.querySelector('[data-step="2"]').hidden = false;
    document.querySelectorAll("[data-step-indicator]").forEach((el) => el.classList.toggle("is-active", el.dataset.stepIndicator === "2"));
    scrollTo({ top: 0, behavior: "smooth" });
  });
  document.getElementById("prevStep").addEventListener("click", () => {
    document.querySelector('[data-step="2"]').hidden = true;
    document.querySelector('[data-step="1"]').hidden = false;
    document.querySelectorAll("[data-step-indicator]").forEach((el) => el.classList.toggle("is-active", el.dataset.stepIndicator === "1"));
  });
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const error = validateBasic();
    if (error) return show(error);
    const payload = {
      loginId: val("loginId"),
      name: val("name"),
      nickname: val("nickname"),
      email: val("email"),
      password: val("password"),
      passwordConfirm: val("passwordConfirm"),
      phone: val("phone"),
      interestJob: val("interestJob"),
      careerType: val("careerType"),
      educationLevel: val("educationLevel"),
      schoolName: val("schoolName"),
      majorName: val("majorName"),
      preferredLocation: val("preferredLocation"),
      termsAgreed: document.getElementById("termsAgreed").checked,
      privacyAgreed: document.getElementById("privacyAgreed").checked,
    };
    try {
      const res = await // 서버 API와 통신할 때 fetch를 사용한다.
      fetch(ctx + "/api/members", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(payload) });
      const data = await res.json();
      if (!res.ok || !data.success) return show(data.message || "회원가입에 실패했습니다.");
      location.href = ctx + (data.redirect || "/member/join/complete");
    } catch (_) {
      show("회원가입 처리 중 오류가 발생했습니다.");
    }
  });
  function validateBasic() {
    if (!/^[a-zA-Z0-9_]{4,20}$/.test(val("loginId"))) return "아이디는 영문, 숫자, 밑줄 4~20자로 입력해주세요.";
    if (!loginIdChecked) return "아이디 중복 확인을 해주세요.";
    if (!val("name")) return "이름을 입력해주세요.";
    if (!val("nickname")) return "닉네임을 입력해주세요.";
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(val("email"))) return "올바른 이메일을 입력해주세요.";
    if (!emailChecked) return "이메일 중복 확인을 해주세요.";
    if (!/^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=])[A-Za-z\d!@#$%^&*()_+\-=]{8,20}$/.test(val("password"))) return "비밀번호는 영문, 숫자, 특수문자를 포함한 8~20자로 입력해주세요.";
    if (val("password") !== val("passwordConfirm")) return "비밀번호가 일치하지 않습니다.";
    if (val("phone") && !/^01[016789]-?\d{3,4}-?\d{4}$/.test(val("phone"))) return "휴대폰 번호 형식을 확인해주세요.";
    if (!document.getElementById("termsAgreed").checked || !document.getElementById("privacyAgreed").checked) return "이용약관과 개인정보 처리방침에 동의해주세요.";
    return "";
  }
  async function get(url) {
    const r = await fetch(ctx + url);
    return r.json();
  }
  function val(id) {
    return document.getElementById(id).value.trim();
  }
  function show(text) {
    msg.textContent = text;
    msg.hidden = !text;
  }
  function help(id, text, ok) {
    const e = document.getElementById(id);
    e.textContent = text;
    e.className = "field-help " + (ok ? "is-ok" : "is-error");
  }
})();
