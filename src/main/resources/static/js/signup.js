document.addEventListener('DOMContentLoaded', function () {
    const usernameInput = document.getElementById('username');
    const nicknameInput = document.getElementById('nickname');
    const passwordInput = document.getElementById('password');
    const confirmPasswordInput = document.getElementById('confirm-password');
    const usernameFeedback = document.getElementById('username-feedback');
    const nicknameFeedback = document.getElementById('nickname-feedback');
    const passwordFeedback = document.getElementById('password-match-feedback');
    const signupButton = document.querySelector('.btn-signup');
    const form = document.querySelector('form');

    const passwordFormatFeedback = document.createElement('div');
    passwordFormatFeedback.id = 'password-format-feedback';
    passwordFormatFeedback.style.fontSize = '14px';
    passwordInput.parentNode.appendChild(passwordFormatFeedback);

    // 이메일 중복 확인
    usernameInput.addEventListener('keyup', function () {
        const username = usernameInput.value.trim();
        if (!username) {
            usernameFeedback.textContent = '';
            return;
        }

        fetch(`/api/member/check-username?username=${encodeURIComponent(username)}`)
            .then(res => res.json())
            .then(data => {
                if (data.exists) {
                    usernameFeedback.textContent = '이미 사용 중인 이메일입니다.';
                    usernameFeedback.style.color = 'red';
                } else {
                    usernameFeedback.textContent = '사용 가능한 이메일입니다!';
                    usernameFeedback.style.color = 'green';
                }
            })
            .catch(() => {
                usernameFeedback.textContent = '서버 오류.';
                usernameFeedback.style.color = 'orange';
            });
    });

    // 닉네임 중복 확인
    nicknameInput.addEventListener('keyup', function () {
        const nickname = nicknameInput.value.trim();
        if (!nickname) {
            nicknameFeedback.textContent = '';
            return;
        }

        fetch(`/api/member/check-nickname?nickname=${encodeURIComponent(nickname)}`)
            .then(res => res.json())
            .then(data => {
                if (data.exists) {
                    nicknameFeedback.textContent = '이미 사용 중인 닉네임입니다.';
                    nicknameFeedback.style.color = 'red';
                } else {
                    nicknameFeedback.textContent = '사용 가능한 닉네임입니다!';
                    nicknameFeedback.style.color = 'green';
                }
            })
            .catch(() => {
                nicknameFeedback.textContent = '서버 오류.';
                nicknameFeedback.style.color = 'orange';
            });
    });

    function isPasswordValid(password) {
        const regex = /^(?=.*[A-Za-z])(?=.*\d).{6,}$/;
        return regex.test(password);
    }

    function validatePassword() {
        const password = passwordInput.value;
        const confirm = confirmPasswordInput.value;

        if (!isPasswordValid(password)) {
            passwordFormatFeedback.textContent = '비밀번호는 문자와 숫자를 포함한 6자 이상이어야 합니다.';
            passwordFormatFeedback.style.color = 'red';
            signupButton.disabled = true;
        } else {
            passwordFormatFeedback.textContent = '사용 가능한 비밀번호입니다!';
            passwordFormatFeedback.style.color = 'green';
        }

        if (!confirm) {
            passwordFeedback.textContent = '';
            return;
        }

        if (password === confirm && isPasswordValid(password)) {
            passwordFeedback.textContent = '비밀번호가 일치합니다.';
            passwordFeedback.style.color = 'green';
            signupButton.disabled = false;
        } else {
            passwordFeedback.textContent = '비밀번호가 일치하지 않습니다.';
            passwordFeedback.style.color = 'red';
            signupButton.disabled = true;
        }
    }

    passwordInput.addEventListener('input', validatePassword);
    confirmPasswordInput.addEventListener('input', validatePassword);

    form.addEventListener('submit', function (e) {
        if (!isPasswordValid(passwordInput.value) || passwordInput.value !== confirmPasswordInput.value) {
            e.preventDefault();
            passwordFeedback.textContent = '비밀번호가 일치하지 않거나 형식이 올바르지 않습니다.';
            passwordFeedback.style.color = 'red';
        }
    });
});