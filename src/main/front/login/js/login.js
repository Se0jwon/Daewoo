document.addEventListener("DOMContentLoaded", function () {
    const toggleIcon = document.querySelector(".passwordbox i");
    const passwordInput = document.querySelector(".passwordbox input[type='password']");

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
});