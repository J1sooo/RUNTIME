document.getElementById("postForm").addEventListener("submit", function(event) {
    event.preventDefault();

    let formData = new FormData();
    formData.append("title", this.title.value);
    formData.append("content", this.content.value);

    const filesInput = this.files;
    if (filesInput && filesInput.files.length > 0) {
        for (let i = 0; i < filesInput.files.length; i++) {
            formData.append("files", filesInput.files[i]);
        }
    }

    const postRequest = {
        title: this.title.value,
        content: this.content.value
    };
    formData.append("post", new Blob([JSON.stringify(postRequest)], { type: 'application/json' }));

    fetch("/api/post", {
        method: "POST",
        body: formData
    })
        .then(response => response.json())
        .then(data => {
            if (data) {
                alert("게시글이 성공적으로 작성되었습니다.");
                window.location.href = "/post";
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("게시글 작성에 실패했습니다.");
        });
});