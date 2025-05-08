// loginStatus.js
document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/member/login-status")
        .then(response => response.json())
        .then(data => {
            const greeting = document.getElementById("greeting");
            const userButtons = document.getElementById("user-buttons");

            if (data.loggedIn) {
                greeting.innerText = `${data.nickname}님 안녕하세요!`;

                userButtons.innerHTML = `
                    <div class="text-end me-3 ml-2">
                        <a href="/mypage" class="btn btn-light text-dark shadow-sm">
                            <i class="bi bi-person"></i>
                        </a>
                    </div>
                    <div class="text-end me-3 ml-2">
                        <button class="btn btn-light text-dark shadow-sm">
                            <i class="bi bi-envelope"></i>
                        </button>
                    </div>
                    <div class="text-end me-3 ml-2 mr-3">
                        <form action="/logout" method="post">
                            <button type="submit" class="btn btn-light text-dark shadow-sm">로그아웃</button>
                        </form>
                    </div>
                `;
            } else {
                greeting.innerText = '';
                userButtons.innerHTML = `
                    <div class="text-end me-3 ml-2 mr-3">
                        <a href="/login" class="btn btn-light text-dark shadow-sm">로그인</a>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error("로그인 상태 확인 실패:", error);
        });
});