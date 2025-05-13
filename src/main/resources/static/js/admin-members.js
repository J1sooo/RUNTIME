// 초기화 함수
document.addEventListener("DOMContentLoaded", function () {
    setupTabSwitch();
    setupMemberSearch();
    setupMemberUpdate();
    fetchMembers();
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