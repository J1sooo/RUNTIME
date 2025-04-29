document.addEventListener('DOMContentLoaded', function () {
    const usernameInput = document.getElementById('username');
    const feedback = document.getElementById('username-feedback');

    usernameInput.addEventListener('keyup', function () {
        const username = usernameInput.value.trim();

        if (username.length === 0) {
            feedback.textContent = '';
            return;
        }

        fetch(`/api/member/check-username?username=${encodeURIComponent(username)}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('서버 통신 에러');
                }
                return response.json();
            })
            .then(data => {
                if (data.exists) {
                    feedback.textContent = '이미 사용 중인 이메일입니다.';
                    feedback.style.color = 'red';
                } else {
                    feedback.textContent = '사용 가능한 이메일입니다!';
                    feedback.style.color = 'green';
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                feedback.textContent = '서버 오류.';
                feedback.style.color = 'orange';
            });
    });
});