document.addEventListener("DOMContentLoaded", function () {
    const submitBtn = document.querySelector(".loginbox");
    const emailInput = document.querySelector("input[name='userName']");

    // 제출 버튼 클릭 시 처리
    submitBtn.addEventListener("click", function (event) {
        event.preventDefault();
        const email = emailInput.value.trim();

        if (!email) {
            alert("이메일을 입력해주세요.");
        } else {
            alert("입력하신 이메일로 임시 비밀번호를 보냈습니다.");

            // 인증 페이지로 이동
            document.body.classList.add("fade-slide-out");
            setTimeout(() => {
                window.location.href = "../../authenticate/html/authenticate.html";
            }, 600);
        }
    });

    // Back to Login 클릭 시 이동
    const backLink = document.querySelector(".back");
    backLink.addEventListener("click", function (e) {
        e.preventDefault();
        document.body.classList.add("fade-slide-out");

        setTimeout(() => {
            window.location.href = "../../login/html/main.html";
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
