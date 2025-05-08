document.addEventListener("DOMContentLoaded", function () {
    const likeBtn = document.getElementById("like-btn");
    const likeCount = document.getElementById("like-count");

    if (!likeBtn || !likeCount) return; // 요소가 없으면 종료

    likeBtn.addEventListener("click", () => {
        const postId = likeBtn.dataset.postId;

        fetch(`/api/posts/${postId}/like`, {
            method: "POST"
        })
            .then(res => res.json())
            .then(data => {
                likeCount.textContent = data.likeCount;
                likeBtn.textContent = "👍";
            })
            .catch(() => alert("로그인이 필요합니다")); // 로그인을 안했을시 좋아요 불가
    });
});