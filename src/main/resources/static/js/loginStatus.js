document.addEventListener("DOMContentLoaded", function () {
    fetch("/api/member/login-status")
        .then(response => response.json())
        .then(async data => {
            const greeting = document.getElementById("greeting");
            const userButtons = document.getElementById("user-buttons");

            if (data.loggedIn) {
                greeting.innerText = `${data.nickname}님 안녕하세요!`;

                let buttonsHTML = `
                    <div class="text-end me-3 ml-2">
                        <a href="/mypage" class="btn btn-light text-dark shadow-sm">
                            <i class="bi bi-person"></i>
                        </a>
                    </div>
                    <a href="/notes" class="text-end me-3 ml-2">
                        <button class="btn btn-light text-dark shadow-sm position-relative">
                            <i id="mail-icon" class="bi bi-envelope"></i>
                        </button>
                    </a>
                `;

                //관리자 여부 확인 후 버튼 추가
                try {
                    const adminRes = await fetch("/api/admin/is-admin");
                    const adminData = await adminRes.json();
                    if (adminData.admin === true) {
                        buttonsHTML += `
                            <div class="text-end me-3 ml-2">
                                <a href="/adminPage" class="btn btn-light text-dark shadow-sm">
                                    <i class="bi bi-gear"></i>
                                </a>
                            </div>
                        `;
                    }
                } catch (e) {
                    console.error("관리자 여부 확인 실패:", e);
                }

                buttonsHTML += `
                    <div class="text-end me-3 ml-2 mr-3">
                        <form action="/logout" method="post">
                            <button type="submit" class="btn btn-light text-dark shadow-sm">로그아웃</button>
                        </form>
                    </div>
                `;

                userButtons.innerHTML = buttonsHTML;

                // 쪽지 확인 비동기
                fetch("/api/note/has-unread")
                    .then(res => res.json())
                    .then(hasUnread => {
                        if (hasUnread) {
                            const icon = document.getElementById("mail-icon");
                            if (icon) {
                                const redDot = document.createElement("span");
                                redDot.className = "position-absolute top-0 start-100 translate-middle p-1 bg-danger border border-light rounded-circle";
                                redDot.style.zIndex = "1";
                                icon.parentElement.appendChild(redDot);
                            }
                        }
                    })
                    .catch(e => console.error("읽지 않은 쪽지 확인 실패:", e));
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