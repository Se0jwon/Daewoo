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
});

// 결제수단
document.addEventListener("DOMContentLoaded", function () {
    // 카드번호 4자리씩 띄어쓰기
    const cardInput = document.getElementById("CNumber");
    cardInput.addEventListener("input", function () {
        let value = this.value.replace(/\D/g, ""); // 숫자만
        value = value.substring(0, 16); // 최대 16자리
        this.value = value.replace(/(\d{4})(?=\d)/g, "$1 "); // 4자리마다 공백
    });

    const payBtn = document.querySelector(".Pay_Add");
    payBtn.addEventListener("click", function (event) {
        event.preventDefault();

        // 입력값
        let CNumber = document.getElementById("CNumber").value.replace(/\s/g, "").trim();
        const Date = document.getElementById("Date").value.trim();
        const Ncard = document.getElementById("Ncard").value.trim();
        const Country = document.getElementById("Country").value.trim();
        const cvc = document.getElementById("cvc").value.trim();
        const agree = document.getElementById("remember").checked;

        // 필수 입력 확인
        if (!CNumber || !Date || !Ncard || !Country || !cvc) {
            alert("모든 항목을 입력해주세요.");
            return;
        }

        // 카드번호 16자리 숫자 확인
        if (!/^\d{16}$/.test(CNumber)) {
            alert("카드번호는 숫자 16자리여야 합니다.");
            return;
        }

        // CVC 3자리 숫자 확인
        if (!/^\d{3}$/.test(cvc)) {
            alert("CVC는 숫자 3자리여야 합니다.");
            return;
        }

        // 약관동의 확인
        if (!agree) {
            alert("약관에 동의 해주세요.");
            return;
        }

        alert("회원가입이 완료되었습니다.");
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
