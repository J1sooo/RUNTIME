document.getElementById("postForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let formData = new FormData(this);

    fetch("/api/post", { // REST 컨트롤러의 저장 API 엔드포인트로 변경
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert("게시글이 성공적으로 작성되었습니다.");
                window.location.href = "/post"; // 저장 성공 후 게시글 목록 페이지로 리다이렉트 (뷰 컨트롤러)
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("게시글 작성에 실패했습니다.");
        });
});