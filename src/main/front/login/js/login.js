document.addEventListener("DOMContentLoaded", function () {
    const toggleIcon = document.querySelector(".passwordbox i");
    const passwordInput = document.querySelector(".passwordbox input[type='password']");
    const loginForm = document.getElementById("loginForm");
    const emailInput = loginForm.querySelector("input[name='userName']");

    // 비밀번호 토글 기능
    toggleIcon.addEventListener("click", function () {
        if (passwordInput.type === "password") {
            passwordInput.type = "text";
            toggleIcon.classList.remove("fa-eye-slash");
            toggleIcon.classList.add("fa-eye");
        } else {
            passwordInput.type = "password";
            toggleIcon.classList.remove("fa-eye");
            toggleIcon.classList.add("fa-eye-slash");
        }
    });

    // 입력값 체크
    loginForm.addEventListener("submit", function (e) {
        if (emailInput.value.trim() === "" || passwordInput.value.trim() === "") {
            e.preventDefault(); // 폼 제출 막기
            alert("이메일과 비밀번호를 모두 입력해주세요!");
        }
    });
});