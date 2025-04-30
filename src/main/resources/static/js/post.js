document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const form = document.getElementById("postForm");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const filesInput = document.getElementById("files");

    if (postId) {
        // 수정 모드
        fetch(`/api/post/${postId}`)
            .then(response => response.json())
            .then(post => {
                titleInput.value = post.title;
                contentInput.value = post.content;

                const submitBtn = document.querySelector("button[type='submit']");
                submitBtn.textContent = "수정";
                submitBtn.type = "button";
                submitBtn.addEventListener("click", () => handleSubmit("PUT", postId));
            })
            .catch(() => alert("게시글 로딩 실패"));
    } else {

        form.addEventListener("submit", (e) => {
            e.preventDefault();
            handleSubmit("POST");
        });
    }

    function handleSubmit(method, id = '') {
        const formData = new FormData();
        formData.append("post", new Blob([JSON.stringify({
            title: titleInput.value,
            content: contentInput.value
        })], { type: "application/json" }));

        Array.from(filesInput.files).forEach(file => formData.append("files", file));

        fetch(`/api/post${id ? `/${id}` : ''}`, {
            method,
            body: formData
        })
            .then(() => {
                alert(method === "PUT" ? "수정이 완료되었습니다" : "등록이 완료되었습니다");
                location.href = `/post${id ? `/${id}` : ''}`;
            })
            .catch(() => alert("게시글 처리에 실패했습니다"));
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const deleteBtn = document.getElementById("deletePostBtn");

    if (!deleteBtn) return console.error("삭제 버튼을 찾지 못했습니다.");

    deleteBtn.addEventListener("click", () => {
        const postId = deleteBtn.dataset.postId;

        if (confirm("정말 이 게시글을 삭제하시겠습니까?")) {
            fetch(`/api/post/${postId}`, { method: "DELETE" })
                .then(res => res.ok ? alert("삭제되었습니다.") : alert("삭제 실패: " + res.status))
                .then(() => window.location.href = "/post")
                .catch(err => {
                    console.error("삭제 오류:", err);
                    alert("삭제에 실패했습니다.");
                });
        }
    });
});
