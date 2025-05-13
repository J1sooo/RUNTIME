document.addEventListener('DOMContentLoaded', () => {
    const postId = document.getElementById('postContainer').getAttribute('data-post-id');
    const commentForm = document.getElementById('commentForm');
    const commentBody = document.getElementById('commentBody');
    const commentList = document.getElementById('commentList'); // 댓글 목록 ul 요소



    // 최상위 댓글 등록 이벤트 리스너 (기존 코드 유지)
    if (commentForm) {
        commentForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const body = commentBody.value;
            const boardId = document.getElementById('boardId').value;

            fetch(`/api/post/${postId}/comments`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ body, boardId})
            })
                .then(res => {
                    if (!res.ok) throw new Error('댓글 저장 실패');
                    return res.json();
                })
                .then(() => {
                    alert("댓글이 등록되었습니다.");
                    window.location.href = `/post/${postId}?board=${boardId}`; // 등록 후 해당 게시글 보기로 이동
                })
                .catch(error => {
                    console.error("댓글 등록 오류:", error);
                    alert("댓글 작성에 실패했습니다.");
                });
        });
    }

    // 대댓글 등록 이벤트 리스너 (이벤트 위임 사용)
    commentList.addEventListener('submit', function (e) {
        if (e.target.classList.contains('replyForm')) {
            e.preventDefault();
            const replyForm = e.target;
            const replyBody = replyForm.querySelector('.replyBody').value;
            const parentCommentId = replyForm.getAttribute('data-parent-id');
            const boardId = document.getElementById('boardId').value;

            fetch(`/api/comments/${parentCommentId}/replies`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ body: replyBody })
            })
                .then(res => {
                    if (!res.ok) throw new Error('대댓글 저장 실패');
                    return res.json();
                })
                .then(() => {
                    alert("답글이 등록되었습니다.");
                    window.location.reload(); // 간단하게 페이지 새로고침
                })
                .catch(error => {
                    console.error("답글 등록 오류:", error);
                    alert("답글 작성에 실패했습니다.");
                });
        }
    });

    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('cancelReplyBtn')) {
            const commentId = e.target.getAttribute('data-parent-id');

            // 댓글 ID에 해당하는 대댓글 폼 찾아서 숨기기
            const replyFormContainer = document.querySelector(`.replyFormContainer[data-parent-id="${commentId}"]`);

            if (replyFormContainer) {
                replyFormContainer.classList.add('d-none'); // 폼 접기
            }
        }
    });

    // "답글" 폼 보이기/숨기기 (선택 사항 - HTML 구조에 따라 다를 수 있음)
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('showReplyFormText')) {
            const commentId = e.target.getAttribute('data-comment-id');

            // 현재 클릭한 span이 포함된 li를 기준으로 replyFormContainer 찾기
            const commentItem = e.target.closest('li');
            const replyFormContainer = commentItem.querySelector(`.replyFormContainer[data-parent-id="${commentId}"]`);

            if (replyFormContainer) {
                replyFormContainer.classList.toggle('d-none');
            }
        }
    });
    // 댓글 수정 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('editCommentBtn')) {
            const commentId = e.target.getAttribute('data-comment-id');
            const bodySpan = document.getElementById(`comment-body-${commentId}`);
            const textarea = document.getElementById(`edit-comment-body-${commentId}`);
            const saveBtn = e.target.parentElement.querySelector('.saveEditBtn');

            bodySpan.classList.add('d-none');
            textarea.classList.remove('d-none');
            saveBtn.classList.remove('d-none');
        }
    });

    // 댓글 저장 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('saveEditBtn')) {
            const commentId = e.target.getAttribute('data-comment-id');
            const newBody = document.getElementById(`edit-comment-body-${commentId}`).value;

            fetch(`/api/comments/${commentId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ body: newBody })
            })
                .then(res => res.json())
                .then(data => {
                    alert('댓글이 수정되었습니다.');
                    document.getElementById(`comment-body-${commentId}`).textContent = data.body;
                    document.getElementById(`comment-body-${commentId}`).classList.remove('d-none');
                    document.getElementById(`edit-comment-body-${commentId}`).classList.add('d-none');
                    e.target.classList.add('d-none');
                })
                .catch(err => {
                    console.error(err);
                    alert('댓글 수정 실패');
                });
        }
    });

    // 댓글 삭제 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('deleteCommentBtn')) {
            const commentId = e.target.getAttribute('data-comment-id');

            if (confirm('댓글을 삭제하시겠습니까?')) {
                fetch(`/api/comments/${commentId}`, {
                    method: 'DELETE'
                })
                    .then(() => {
                        alert('댓글이 삭제되었습니다.');
                        e.target.closest('li').remove();
                    })
                    .catch(err => {
                        console.error(err);
                        alert('댓글 삭제 실패');
                    });
            }
        }
    });

    // 대댓글 수정 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('editReplyBtn')) {
            const replyId = e.target.getAttribute('data-reply-id');
            const bodyP = document.getElementById(`reply-body-${replyId}`);
            const textarea = document.getElementById(`edit-reply-body-${replyId}`);
            const saveBtn = e.target.parentElement.querySelector('.saveEditReplyBtn');

            bodyP.classList.add('d-none');
            textarea.classList.remove('d-none');
            saveBtn.classList.remove('d-none');
        }
    });

    // 대댓글 저장 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('saveEditReplyBtn')) {
            const replyId = e.target.getAttribute('data-reply-id');
            const newBody = document.getElementById(`edit-reply-body-${replyId}`).value;

            fetch(`/api/comments/${replyId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ body: newBody })
            })
                .then(res => res.json())
                .then(data => {
                    alert('대댓글이 수정되었습니다.');
                    document.getElementById(`reply-body-${replyId}`).textContent = data.body;
                    document.getElementById(`reply-body-${replyId}`).classList.remove('d-none');
                    document.getElementById(`edit-reply-body-${replyId}`).classList.add('d-none');
                    e.target.classList.add('d-none');
                })
                .catch(err => {
                    console.error(err);
                    alert('대댓글 수정 실패');
                });
        }
    });

    // 대댓글 삭제 버튼 클릭
    commentList.addEventListener('click', function (e) {
        if (e.target.classList.contains('deleteReplyBtn')) {
            const replyId = e.target.getAttribute('data-reply-id');

            if (confirm('대댓글을 삭제하시겠습니까?')) {
                fetch(`/api/comments/${replyId}`, {
                    method: 'DELETE'
                })
                    .then(() => {
                        alert('대댓글이 삭제되었습니다.');
                        e.target.closest('li').remove();
                    })
                    .catch(err => {
                        console.error(err);
                        alert('대댓글 삭제 실패');
                    });
            }
        }
    });

});