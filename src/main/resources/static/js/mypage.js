document.addEventListener("DOMContentLoaded", function () {

    document.querySelector('#update-btn').addEventListener('click', async () => {
        const username = document.querySelector('#username').value;
        const currentPassword = document.querySelector('#current-password').value;
        const newNickname = document.querySelector('#nickname').value;
        const newPassword = document.querySelector('#new-password').value;

        const response = await fetch('/api/member/update-user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: username,
                password: currentPassword,
                new_nickname: newNickname,
                new_password: newPassword
            })
        });

        const result = await response.json();

        if (response.ok) {
            alert(result.message || "수정 완료되었습니다.");
            window.location.href = "/index";
        } else {
            alert(result.message || "수정에 실패했습니다.");
        }
    });

    // 회원 탈퇴 요청
    document.querySelector('#delete-btn').addEventListener('click', async () => {
        if (!confirm("정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.")) return;

        const username = document.querySelector('#username').value;
        const currentPassword = document.querySelector('#delete-password').value;

        const response = await fetch('/api/member/delete-user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: username,
                password: currentPassword
            })
        });

        const result = await response.json();
        alert(result.message || "탈퇴 완료되었습니다.");

        if (response.ok) {
            window.location.href = "/login"; // 탈퇴 후 로그인 페이지로 이동
        }
    });
});