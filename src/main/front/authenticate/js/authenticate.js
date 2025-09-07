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
        //   AJAX로 연결?
    });
});
