document.addEventListener("DOMContentLoaded", function () {
    const submitBtn = document.querySelector(".loginbox");
    const emailInput = document.querySelector("input[name='userName']");

    submitBtn.addEventListener("click", function (event) {
        event.preventDefault();
        const email = emailInput.value.trim();

        if (!email) {
            alert("이메일을 입력해주세요.");
        } else {
            alert("입력하신 이메일로 비밀번호 재설정 안내를 보냈습니다.");
        }
    });

    // 회원가입을 하고 회원 된 이메일로 비밀번호를 찾기 할 때, 다른 이메일로입력하는 경우
    // 해당 이메일 없다는 문구 나중에 구현
});
