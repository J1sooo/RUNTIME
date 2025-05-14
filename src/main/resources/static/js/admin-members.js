// 초기화 함수
document.addEventListener("DOMContentLoaded", function () {
    setupTabSwitch();
    setupMemberSearch();
    setupMemberUpdate();
    fetchMembers();
    setupPostManagement();
});

// 회원 목록 조회
async function fetchMembers(query = "") {
    const res = await fetch(`/api/admin/get-members${query}`);
    const data = await res.json();

    const memberBody = document.getElementById("member-body");
    memberBody.innerHTML = "";
    data.members.forEach(member => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${member.id}</td>
            <td>${member.nickname}</td>
            <td>${member.username}</td>
            <td>${member.levelNo}</td>
            <td>
                <select class="form-control d-inline-block w-auto" data-id="${member.id}">
                    <option value="1">브론즈</option>
                    <option value="2">실버</option>
                    <option value="3">골드</option>
                </select>
                <button class="btn btn-sm btn-success ml-1 update-btn" data-id="${member.id}">변경</button>
                <button class="btn btn-sm btn-info ml-1 detail-btn" data-id="${member.id}">상세</button>
            </td>
        `;
        memberBody.appendChild(tr);
    });
}

// 회원 등급 변경
function setupMemberUpdate() {
    const memberBody = document.getElementById("member-body");
    memberBody.addEventListener("click", async e => {
        if (e.target.classList.contains("update-btn")) {
            const id = e.target.dataset.id;
            const select = document.querySelector(`select[data-id="${id}"]`);
            const levelNum = parseInt(select.value);

            const request = JSON.stringify({ member_id: Number(id), level: levelNum });
            const res = await fetch("/api/admin/change-user-level", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: request
            });

            const result = await res.json();
            alert(result.message || "등급이 변경되었습니다.");
            fetchMembers();
        }

        if (e.target.classList.contains("detail-btn")) {
            const id = e.target.dataset.id;
            fetchMemberDetail(id);
        }
    });
}

// 회원 검색
function setupMemberSearch() {
    document.getElementById("search-form").addEventListener("submit", e => {
        e.preventDefault();
        const id = document.getElementById("search-id").value.trim();
        const username = document.getElementById("search-username").value.trim();
        const nickname = document.getElementById("search-nickname").value.trim();

        const query = `?id=${id || -1}&username=${encodeURIComponent(username)}&nickname=${encodeURIComponent(nickname)}`;
        fetchMembers(query);
    });
}

// 탭 전환 (게시물, 회원정보 조회 스위치)
function setupTabSwitch() {
    document.getElementById("show-members-btn").addEventListener("click", () => {
        document.getElementById("member-section").style.display = "block";
        document.getElementById("post-section").style.display = "none";
    });
    document.getElementById("show-posts-btn").addEventListener("click", () => {
        document.getElementById("member-section").style.display = "none";
        document.getElementById("post-section").style.display = "block";
    });
}

// 상세 정보 조회 ( 유저의 상세 정보)
async function fetchMemberDetail(memberId) {
    const res = await fetch(`/api/admin/get-logs?id=${memberId}`);
    const data = await res.json();

    if (data.responseCode === "OK") {
        alert(
            `ID: ${memberId}\n` +
            `이메일: ${data.member.username}\n` +
            `닉네임: ${data.member.nickname}\n` +
            `가입일: ${data.joinDate || "-"}\n` +
            `최종 접속: ${data.logs[0]?.timeStamp || "-"}\n` +
            `총 접속 횟수: ${data.currentLoginCount || 0}\n` +
            `접속 환경: ${data.logs[0]?.userAgent || "-"}`
        );
    } else {
        alert(data.message || "회원 정보를 불러오지 못했습니다.");
    }
}


    //게시물관리
function setupPostManagement() {
    const postBody = document.getElementById("post-body");
    const searchInput = document.getElementById("post-search-input");
    const searchBtn = document.getElementById("post-search-btn");
    const boardFilter = document.getElementById("board-filter");
    const postSection = document.getElementById("post-section");
    const sortOrderClient = document.getElementById("sort-order-client");
    const showPostsBtn = document.getElementById("show-posts-btn");
    const memberSection = document.getElementById("member-section");
    document.getElementById("load-all-posts").addEventListener("click", fetchAllPostsPaginated);

    let allPosts = [];
    let currentPosts = [];

    function getBoardName(boardId) {
        switch (boardId) {
            case 1: return "공지사항";
            case 2: return "소통게시판";
            case 3: return "운동일지";
            case 4: return "크루메인";
            case 5: return "크루공지";
            case 6: return "크루소통";
            default: return "알 수 없음";
        }
    }

    async function fetchPosts(query = "") {
        const res = await fetch(`/api/post${query}`);
        const data = await res.json();
        allPosts = data.content;
        currentPosts = [...allPosts];
        renderPosts(currentPosts);
    }

    function renderPosts(posts) {
        console.log("renderPosts 호출:", posts.length, "게시글");
        postBody.innerHTML = "";
        posts.forEach(post => {
            console.log("렌더링:", post.id, post.title, new Date(post.createdAt).toLocaleString());
            const boardName = getBoardName(post.boardId);
            const tr = document.createElement("tr");
            const createdAt = new Date(post.createdAt);
            const formattedDate = createdAt.toLocaleDateString('ko-KR');
            tr.innerHTML = `            
                <td>${post.id}</td>
                <td>${boardName}</td>
                <td><a href="/post/${post.id}?board=${post.boardId}">${post.title}</a></td>
                <td>${post.author.nickname}</td>
                <td>${formattedDate}</td>
            `;
            postBody.appendChild(tr);
        });
    }

    async function searchPosts() {
        const titleKeyword = document.getElementById("post-search-input").value.trim();
        const authorKeyword = document.getElementById("post-author-input").value.trim();
        const boardId = document.getElementById("board-filter").value;

        let query = `/posts/search?`;

        if (titleKeyword) {
            query += `title=${encodeURIComponent(titleKeyword)}&`;
        }

        if (authorKeyword) {
            query += `nickname=${encodeURIComponent(authorKeyword)}&`;
        }

        if (boardId) {
            query += `boardId=${boardId}`;
        }

        const res = await fetch(query);
        const data = await res.json();
        currentPosts = data.content;
        renderPosts(currentPosts);
    }
    async function fetchAllPostsPaginated() {
        let page = 0;
        const size = 10;
        let allPosts = [];
        let hasNext = true;

        while (hasNext) {
            try {
                const res = await fetch(`/api/post?page=${page}&size=${size}`);
                const data = await res.json();

                allPosts = allPosts.concat(data.content);

                if (data.last || data.content.length === 0) {
                    hasNext = false;
                } else {
                    page++;
                }
            } catch (error) {
                console.error("전체 게시글 불러오기 실패:", error);
                break;
            }
        }

        currentPosts = allPosts;
        renderPosts(currentPosts);
    }


    async function fetchAllPostsPaginated() {
        let page = 0;
        const size = 10;
        let allPosts = [];
        let hasNext = true;

        while (hasNext) {
            try {
                const res = await fetch(`/api/post?page=${page}&size=${size}`);
                const data = await res.json();

                allPosts = allPosts.concat(data.content);

                if (data.last || data.content.length === 0) {
                    hasNext = false;
                } else {
                    page++;
                }
            } catch (error) {
                console.error("전체 게시글 불러오기 실패:", error);
                break;
            }
        }

        currentPosts = allPosts;
        renderPosts(currentPosts);
    }

    function sortPosts(order) {
        console.log("sortPosts 호출:", order);
        if (!order) {
            renderPosts([...currentPosts]);
            return;
        }

        const sortedPosts = [...currentPosts].sort((a, b) => {
            const dateA = new Date(a.createdAt);
            const dateB = new Date(b.createdAt);
            return order === 'asc' ? dateA - dateB : dateB - dateA;
        });

        console.log("정렬 후:", sortedPosts.map(post => post.createdAt));
        renderPosts(sortedPosts);
    }

    showPostsBtn.addEventListener("click", () => {
        memberSection.style.display = "none";
        postSection.style.display = "block";
        fetchPosts();
    });

    searchBtn.addEventListener("click", (e) => {
        e.preventDefault();
        searchPosts();
    });

    if (document.getElementById("sort-order-client")) {
        document.getElementById("sort-order-client").addEventListener("change", (event) => {
            sortPosts(event.target.value);
        });
    }

    fetchPosts();
}