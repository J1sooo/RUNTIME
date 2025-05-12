document.addEventListener("DOMContentLoaded", function () {
    const memberBody = document.getElementById("member-body");

    async function fetchMembers(query = "") {
        const res = await fetch(`/api/admin/get-members${query}`);
        const data = await res.json();

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
                </td>
            `;
            memberBody.appendChild(tr);
        });
    }

    fetchMembers();

    document.getElementById("search-form").addEventListener("submit", e => {
        e.preventDefault();
        const id = document.getElementById("search-id").value.trim();
        const username = document.getElementById("search-username").value.trim();
        const nickname = document.getElementById("search-nickname").value.trim();

        const query = `?id=${id || -1}&username=${encodeURIComponent(username)}&nickname=${encodeURIComponent(nickname)}`;
        fetchMembers(query);
    });

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
    });

    document.getElementById("show-members-btn").addEventListener("click", () => {
        document.getElementById("member-section").style.display = "block";
        document.getElementById("post-section").style.display = "none";
    });

    document.getElementById("show-posts-btn").addEventListener("click", () => {
        document.getElementById("member-section").style.display = "none";
        document.getElementById("post-section").style.display = "block";
    });
});