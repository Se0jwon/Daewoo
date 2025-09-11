document.addEventListener("DOMContentLoaded", function () {
    const toggleIcon = document.querySelector(".passwordbox i");
    const passwordInput = document.querySelector(".passwordbox input[type='password']");
    const loginForm = document.getElementById("loginForm");
    const emailInput = loginForm.querySelector("input[name='userName']");

    // 비밀번호
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

    // 입력
    loginForm.addEventListener("submit", function (e) {
        if (emailInput.value.trim() === "" || passwordInput.value.trim() === "") {
            e.preventDefault();
            alert("이메일과 비밀번호를 모두 입력해주세요!");
        }
    });
});
// 회원가입 페이지 이동
document.addEventListener("DOMContentLoaded", function () {
    const signupLink = document.querySelector(".signup a");

    signupLink.addEventListener("click", function (e) {
        e.preventDefault();
        document.body.classList.add("fade-slide-out");
        setTimeout(() => {
            window.location.href = signupLink.href;
        }, 600);
    });
});

// 비밀번호찾기 페이지 이동
document.addEventListener("DOMContentLoaded", function () {
    const forgotLink = document.querySelector(".text2 a");

    forgotLink.addEventListener("click", function (e) {
        e.preventDefault();
        document.body.classList.add("fade-slide-out");

        setTimeout(() => {
            window.location.href = forgotLink.href;
        }, 600);
    });
});
// 이미지 슬라이드
document.addEventListener("DOMContentLoaded", function () {
    const slides = document.querySelectorAll(".slide");
    const dots = document.querySelectorAll(".dot");
    let index = 0;

    function showSlide(n) {
        slides.forEach((slide, i) => {
            slide.classList.toggle("active", i === n);
            dots[i].classList.toggle("active", i === n);
        });
        index = n;
    }

    function nextSlide() {
        index = (index + 1) % slides.length;
        showSlide(index);
    }


    showSlide(index);

    // 3초 자동 이미지 전환
    setInterval(nextSlide, 3000);
});
