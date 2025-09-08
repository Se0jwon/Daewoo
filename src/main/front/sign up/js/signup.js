document.addEventListener("DOMContentLoaded", function () {
    const toggleIcons = document.querySelectorAll(".toggle-password");
    const signupBtn = document.querySelector(".loginbox");

    // 비밀번호 보이기/숨기기
    toggleIcons.forEach(icon => {
        icon.addEventListener("click", function () {
            const targetId = this.getAttribute("data-target");
            const input = document.getElementById(targetId);

            if (input.type === "password") {
                input.type = "text";
                this.classList.remove("fa-eye-slash");
                this.classList.add("fa-eye");
            } else {
                input.type = "password";
                this.classList.remove("fa-eye");
                this.classList.add("fa-eye-slash");
            }
        });
    });

    // 회원가입 버튼 클릭
    signupBtn.addEventListener("click", function (event) {
        event.preventDefault();

        const fname = document.getElementById("fname").value.trim();
        const lname = document.getElementById("lname").value.trim();
        const email = document.getElementById("Email").value.trim();
        const phone = document.getElementById("pNum").value.trim();
        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();
        const agree = document.getElementById("remember").checked;

        if (!fname || !lname || !email || !phone || !password || !confirmPassword) {
            alert("모든 항목을 입력해주세요.");
        } else if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
        } else if (!agree) {
            alert("약관에 동의 해주세요.");
        } else {
            alert("회원가입이 완료되었습니다.");
        }
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
