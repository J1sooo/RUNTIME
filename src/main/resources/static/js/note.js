const createButton = document.getElementById("noteBtn");

if (createButton) {
    createButton.addEventListener('click', () => {
        fetch(`/api/note`, {
            method: 'POST',
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                receiver: document.getElementById('receiver').value,
                message: document.getElementById('message').value
            })
        }).then(res => {
            if (!res.ok) {
                throw new Error("전송을 실패했습니다");
            }
            alert("쪽지가 전송되었습니다.");
            location.replace(`/notes`);
        }).catch(error => {
            alert("에러 발생: " + error.message);
        })
    });
}