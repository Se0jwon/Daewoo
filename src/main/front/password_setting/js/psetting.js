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
