let editor;

document.addEventListener("DOMContentLoaded", () => {
    const editorElement = document.querySelector("#toastEditor");
    if (!editorElement) return; // toastEditor가 없으면 실행하지 않음

    // toastEditor가 존재하면 에디터 초기화
    editor = new toastui.Editor({
        el: editorElement,
        height: "500px",
        initialEditType: "wysiwyg",
        previewStyle: "vertical",
        toolbarItems: [
            ['heading', 'bold', 'italic', 'quote'],
            ['ul', 'ol'],
            ['link', 'image']
        ],
        hooks: {
            addImageBlobHook: (blob, callback) => {
                uploadImage(blob, callback);
                return false;
            }
        }
    });

    const params = new URLSearchParams(window.location.search);
    const postId = params.get("id");
    const form = document.getElementById("postForm");
    const titleInput = document.getElementById("title");
    const contentInput = document.getElementById("content");
    const filesInput = document.getElementById("files");
    const submitBtn = document.getElementById("submitBtn");

    const boardIdFromUrl = params.get("board");
    if (boardIdFromUrl) {
        const boardInput = document.querySelector("input[name='boardId']");
        if (boardInput) {
            boardInput.value = boardIdFromUrl;
        }
    }

    if (postId) {
        // 수정 모드
        fetch(`/api/post/${postId}`)
            .then(response => response.json())
            .then(post => {
                titleInput.value = post.title;
                editor.setHTML(post.content);

                submitBtn.textContent = "수정";
                submitBtn.type = "button";
                submitBtn.addEventListener("click", () => handleSubmit("PUT", postId));
            })
            .catch(() => alert("게시글 로딩 실패"));
    } else {
        // 등록 모드
        form.addEventListener("submit", (e) => {
            e.preventDefault();
            handleSubmit("POST");
        });
    }
    function handleSubmit(method, id = '') {
        const htmlContent = editor.getHTML();
        console.log("최종 HTML:", htmlContent);

        contentInput.value = htmlContent; // 반드시 FormData 생성 전에 반영

        const boardId = document.querySelector("input[name='boardId']").value;

        if (boardId === "3" && !htmlContent.includes('<img ')) {
            alert("게시글을 등록하려면 이미지를 첨부해야 합니다.");
            return; // 이미지가 첨부되지 않았으면 제출을 중단
        }

        const formData = new FormData();

        // JSON으로 포장하여 append
        formData.append("post", new Blob([JSON.stringify({
            title: titleInput.value,
            content: contentInput.value
        })], { type: "application/json" }));

        Array.from(filesInput.files).forEach(file => formData.append("files", file));



        const url = method === "POST"
            ? `/api/board/${boardId}/post`
            : `/api/post/${id}`;

        fetch(url, {
            method,
            body: formData
        })
            .then(response => {
                if (!response.ok) throw new Error("서버 응답 오류");
                return response.json();
            })
            .then(() => {
                alert(method === "PUT" ? "수정이 완료되었습니다" : "등록이 완료되었습니다");
                const boardId = document.querySelector("input[name='boardId']").value;
                if (boardId === "3") {
                    location.href = `/post?board=3`;
                } else {
                    // PUT 또는 POST 후 다른 boardId로 리디렉션
                    if (method === "PUT") {
                        location.href = `/post/${id}?board=${boardId}`;
                    } else {
                        location.href = `/post?board=${boardId}`;
                    }
                }
            })
            .catch(err => {
                console.error("에러 발생:", err);
                alert("게시글 처리에 실패했습니다.(글자수 확인)");
            });
    }

    function uploadImage(blob, callback) {
        const formData = new FormData();
        formData.append("file", blob);

        fetch('/api/image/upload', {
            method: 'POST',
            body: formData,
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => {
                if (data.imageUrl) {

                    callback(data.imageUrl);
                } else {
                    callback('');
                }
            })
            .catch(() => {
                alert('이미지 업로드 실패');
                return;
            });
    }
});

document.addEventListener("DOMContentLoaded", () => {
    const deleteBtn = document.getElementById("deletePostBtn");

    if (!deleteBtn) return console.error("삭제 버튼을 찾지 못했습니다.");

    deleteBtn.addEventListener("click", () => {
        const postId = deleteBtn.dataset.postId;

        const params = new URLSearchParams(window.location.search);
        const boardId = params.get("board");

        if (confirm("정말 이 게시글을 삭제하시겠습니까?")) {
            fetch(`/api/post/${postId}`, { method: "DELETE" })
                .then(res => res.ok ? alert("삭제되었습니다.") : alert("삭제 실패: " + res.status))
                .then(() => window.location.href = `/post?board=${boardId}`)
                .catch(err => {
                    console.error("삭제 오류:", err);
                    alert("삭제에 실패했습니다.");
                });
        }
    });
});
