/*
=========================================================
파일 설명
=========================================================
로그인 화면 JavaScript입니다.
로그인 폼 값을 JSON으로 /api/auth/login에 전송하고 성공 시 서버가 알려준 주소로 이동합니다.
*/

(() => {
  const ctx = window.JOBON_CTX || "";
  const form = document.getElementById("loginForm");
  const msg = document.getElementById("loginMessage");
  const loginId = document.getElementById("loginId");
  const remember = document.getElementById("rememberId");
  const saved = localStorage.getItem("jobonSavedLoginId");
  if (saved) {
    loginId.value = saved;
    remember.checked = true;
  }

  form?.addEventListener("submit", async (e) => {
    e.preventDefault();
    show("");
    const payload = { loginId: loginId.value.trim(), password: document.getElementById("password").value };
    if (!payload.loginId || !payload.password) return show("아이디와 비밀번호를 모두 입력해주세요.");
    try {
      const res = await fetch(ctx + "/api/auth/login", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(payload) });
      const data = await res.json();
      if (!res.ok || !data.success) return show(data.message || "로그인에 실패했습니다.");
      if (remember.checked) localStorage.setItem("jobonSavedLoginId", payload.loginId);
      else localStorage.removeItem("jobonSavedLoginId");
      location.href = data.redirect || ctx + "/dashboard";
    } catch (_) {
      show("로그인 처리 중 오류가 발생했습니다.");
    }
  });
  function show(text) {
    msg.textContent = text;
    msg.hidden = !text;
  }
})();
