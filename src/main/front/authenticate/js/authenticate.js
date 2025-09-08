document.addEventListener("DOMContentLoaded", function () {
    const toggleIcons = document.querySelectorAll(".toggle-password");

    // 눈 아이콘 토글
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

    // 재전송하기
    const resendBtn = document.querySelector(".resend");
    resendBtn.addEventListener("click", function () {
        alert("인증 된 이메일로 재전송되었습니다. 메일을 확인하시길 바랍니다." +
            "------공사중------");
    });

    // 페이지 이동
    const backLink = document.querySelector(".back");
    backLink.addEventListener("click", function (e) {
        e.preventDefault();
        document.body.classList.add("fade-slide-out");

        setTimeout(() => {
            window.location.href = "../../login/html/main.html";
        }, 600);
    });

    // // 인증완료
    // const authenticate = document.querySelector(".certified");
    // authenticate.addEventListener("click", function (e) {
    //     e.preventDefault();
    //
    //     const codeInput = document.getElementById("code").value.trim();
    //
    //     if (!codeInput) {
    //         alert("인증번호를 입력해주세요.");
    //         return;
    //     }
    //
    //     alert("인증되었습니다.");
    // });
});

// 비밀번호 설정페이지로 이동
const authenticate = document.querySelector(".certified");
authenticate.addEventListener("click", function (e) {
    e.preventDefault();

    const codeInput = document.getElementById("code").value.trim();

    if (!codeInput) {
        alert("인증번호를 입력해주세요.");
        return;
    }

    document.body.classList.add("fade-slide-out");
    setTimeout(() => {
        window.location.href = "../../password_setting/html/psetting.html";
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

