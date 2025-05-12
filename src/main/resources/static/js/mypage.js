document.addEventListener("DOMContentLoaded", function () {
    const usernameInput = document.getElementById('username');
    const currentPasswordInput = document.getElementById('current-password');
    const nicknameInput = document.getElementById('nickname');
    const newPasswordInput = document.getElementById('new-password');
    const confirmPasswordInput = document.getElementById('confirm-new-password');
    const nicknameFeedback = document.getElementById('nickname-feedback');
    const passwordFeedback = document.getElementById('password-feedback');
    const updateButton = document.getElementById('update-btn');
    const deleteButton = document.getElementById('delete-btn');
    const originalNickname = nicknameInput.value.trim();

    function isPasswordValid(password) {
        const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;
        return regex.test(password);
    }

    function validateNewPassword() {
        const password = newPasswordInput.value;
        const confirm = confirmPasswordInput.value;

        if (!password && !confirm) {
            passwordFeedback.textContent = '';
            updateButton.disabled = false;
            return;
        }

        if (password && !isPasswordValid(password)) {
            passwordFeedback.textContent = '비밀번호는 문자와 숫자를 포함한 6자 이상이어야 합니다.';
            passwordFeedback.style.color = 'red';
            updateButton.disabled = true;
            return;
        }

        if (password !== confirm) {
            passwordFeedback.textContent = '비밀번호가 서로 일치하지 않습니다.';
            passwordFeedback.style.color = 'red';
            updateButton.disabled = true;
        } else {
            passwordFeedback.textContent = '비밀번호가 일치합니다.';
            passwordFeedback.style.color = 'green';
            updateButton.disabled = false;
        }
    }

    nicknameInput.addEventListener('keyup', function () {
        const nickname = nicknameInput.value.trim();

        if (!nickname || nickname === originalNickname) {
            nicknameFeedback.textContent = '';
            updateButton.disabled = false;
            return;
        }

        fetch(`/api/member/check-nickname?nickname=${encodeURIComponent(nickname)}`)
            .then(res => res.json())
            .then(data => {
                if (data.exists) {
                    nicknameFeedback.textContent = '이미 사용 중인 닉네임입니다.';
                    nicknameFeedback.style.color = 'red';
                    updateButton.disabled = true;
                } else {
                    nicknameFeedback.textContent = '사용 가능한 닉네임입니다!';
                    nicknameFeedback.style.color = 'green';
                    updateButton.disabled = false;
                }
            })
            .catch(() => {
                nicknameFeedback.textContent = '서버 오류.';
                nicknameFeedback.style.color = 'orange';
                updateButton.disabled = true;
            });
    });

    newPasswordInput.addEventListener('input', validateNewPassword);
    confirmPasswordInput.addEventListener('input', validateNewPassword);

    updateButton.addEventListener('click', async () => {
        const newNickname = nicknameInput.value.trim();
        const newPassword = newPasswordInput.value.trim();
        const confirmPassword = confirmPasswordInput.value.trim();

        if (newPassword && !isPasswordValid(newPassword)) {
            passwordFeedback.textContent = '비밀번호는 문자와 숫자를 포함한 6자 이상이어야 합니다.';
            passwordFeedback.style.color = 'red';
            return;
        }

        if (newPassword && newPassword !== confirmPassword) {
            passwordFeedback.textContent = '비밀번호가 서로 일치하지 않습니다.';
            passwordFeedback.style.color = 'red';
            return;
        }

        const res = await fetch('/api/member/update-user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: usernameInput.value,
                password: currentPasswordInput.value,
                new_nickname: newNickname,
                new_password: newPassword
            })
        });

        const result = await res.json();
        alert(result.message);
    });

    deleteButton.addEventListener('click', async () => {
        if (!confirm("정말 탈퇴하시겠습니까?")) return;

        const deletePassword = document.getElementById('delete-password').value;

        const res = await fetch('/api/member/delete-user', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: usernameInput.value,
                password: deletePassword
            })
        });

        const result = await res.json();
        alert(result.message || "탈퇴 완료되었습니다.");

        if (res.ok) {
            window.location.href = "/login";
        }
    });
});