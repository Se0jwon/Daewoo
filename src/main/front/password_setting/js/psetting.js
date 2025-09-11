document.addEventListener("DOMContentLoaded", function () {
    const toggleIcons = document.querySelectorAll(".toggle-password");
    const submitBtn = document.querySelector(".submit");

    // 눈 아이콘 클릭 이벤트
    toggleIcons.forEach(icon => {
        icon.addEventListener("click", function () {
            const targetId = this.getAttribute("data-target");
            const input = document.getElementById(targetId);

            if (input.type === "password") {
                input.type = "text";
                this.classList.remove("fa-eye");
                this.classList.add("fa-eye-slash");
            } else {
                input.type = "password";
                this.classList.remove("fa-eye-slash");
                this.classList.add("fa-eye");
            }
        });
    });

    //  비밀번호 설정
    submitBtn.addEventListener("click", function (event) {
        event.preventDefault();

        const password = document.getElementById("password").value.trim();
        const confirmPassword = document.getElementById("confirmPassword").value.trim();

        if (!password || !confirmPassword) {
            alert("비밀번호를 입력해주세요.");
        } else if (password !== confirmPassword) {
            alert("비밀번호가 일치하지 않습니다.");
        } else {
            alert("비밀번호가 설정되었습니다.");
        }
    });
});
    // 비밀번호 설정하고 로그인 페이지로 이동
const authenticate = document.querySelector(".submit");
authenticate.addEventListener("click", function (e) {
    e.preventDefault();


    document.body.classList.add("fade-slide-out");
    setTimeout(() => {
        window.location.href = "../../login/html/main.html";
    }, 600);
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
