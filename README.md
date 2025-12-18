# 🏃‍♂️ RUNTIME


## 📌 프로젝트 소개
**RUNTIME**은 조깅을 즐기는 사람들이 모여 소통하고, 정보를 공유하고, 함께 운동할 수 있는 커뮤니티입니다.
- **팀명**: 런타임(RUNTIME)  
- **의미**: 개발자에게는 ‘실행 시간(Run-Time)’, 일반인에게는 ‘달리기(Run Time)’를 의미하는 이중적 상징성을 담고 있음  
- **총 작업 기간**: 2024.04.18 - 2024.05.13  
- **작업 인원**: 4명


## 👥 팀원
- [@BanditKing](https://github.com/BanditKing)  
- [@jameskdev](https://github.com/jameskdev)  
- [@jdoitja](https://github.com/jdoitja)


## 🌐 배포 사이트 (서버 운영 종료)
🔗 [http://www.est-runtime.com/index](http://www.est-runtime.com/index)


## 😎 Stack
### 💻 프론트엔드
![HTML](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Bootstrap](https://img.shields.io/badge/Bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)


### 🔧 백엔드
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/JPA-59666C?style=for-the-badge&logo=hibernate&logoColor=white)


### 🛢️ 데이터베이스
![MySQL](https://img.shields.io/badge/AWS_RDS_MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![H2](https://img.shields.io/badge/H2-006699?style=for-the-badge&logo=h2&logoColor=white)


### 🧪 기타
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)
![Git](https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white)
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white)
![AWS EC2](https://img.shields.io/badge/AWS_EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![AWS S3](https://img.shields.io/badge/AWS_S3-569A31?style=for-the-badge&logo=amazonaws&logoColor=white)


## 🎨 초기 디자인
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)</br>
🔗 [Figma 바로 보기](https://www.figma.com/design/AuCkZYUEtWlnnZltLP6NHF/Untitled?node-id=0-1&t=iolWqpjJz4xyJb6V-1)


## 🗂️ ERD
![ERDCloud](https://img.shields.io/badge/ERDCloud-7D7D7D?style=for-the-badge)</br>
🔗 [ERD 바로 보기](https://www.erdcloud.com/d/N6KvohBxZGvbCeiMh)

## ⚙️ RUNTIME 시스템 다이어그램
<p align="center">
  <img width="483" height="527" alt="Runtime 시스템 다이어그램" src="https://github.com/user-attachments/assets/59a37ece-65e8-4c3a-a2fe-dc871d12c2e0" />
</p>

## 🔑 핵심 기능

- 게시판 CRUD (제목, 본문, 작성자 등)  
- 회원 CRUD (회원가입, 수정, 탈퇴)  
- 게시글 댓글 및 대댓글 기능
- 게시글 좋아요 기능
- 회원간 팔로우 기능
- 회원간 쪽지 기능  
- 등급별 권한: 회원 가입 → 관리자 승인 → 단계별 기능 제공  
- 이미지 업로드: 게시글 이미지 첨부 (S3 저장소 사용)
- EC2 배포 (Route53, Nginx 사용)



## 👥 사용자 권한 안내

| 구분       | 권한 및 기능 |
|------------|--------------|
| 비회원     | 홈, 공지사항, 소통 게시판, 운동일지 리스트만 열람 가능 |
| 1단계 회원 | 공지사항, 소통 게시판, 운동일지 작성 가능 |
| 2단계 회원 | 크루 활동, 쪽지 이용 가능 |
| 3단계 회원 | 크루 모집 글 작성, 크루 공지 작성 가능 |
| 관리자     | 전체 회원/게시글/공지사항 관리, 권한 부여 및 회수 가능 |



## 🧾 API 명세서

<details>
<summary>👑 관리자 API</summary>

| 메서드명 | HTTP | URL | 설명 |
|----------|------|-----|------|
| getMembers | GET | /api/admin/get-members | 전체 회원 목록 또는 조건 검색 |
| getLevels | GET | /api/admin/get-levels | 유저 등급 목록 조회 |
| requestAdmin | POST | /api/admin/request-admin | 어드민 권한 요청 처리 |
| createAuthority | POST | /api/admin/create-authority | 어드민 권한 생성 |
| addAuthorityForLevel | POST | /api/admin/add-authority-for-level | 등급에 권한 추가 |
| removeAuthority | POST | /api/admin/remove-authority-for-level | 등급에서 권한 제거 |
| createUserLevel | POST | /api/admin/create-user-level | 새로운 유저 등급 생성 |
| changeUserLevel | POST | /api/admin/change-user-level | 회원 등급 변경 |
| getMethodName | GET | /api/admin/is-admin | 어드민 여부 확인 |
| getLogsForMember | GET | /api/admin/get-logs | 회원 로그 조회 (기간 필터링) |
| getAdminRequests | GET | /api/admin/get-admin-requests | 어드민 권한 요청 목록 조회 |

</details>

<details>
<summary>🙋 회원 API</summary>

| 메서드명 | HTTP | URL | 설명 |
|----------|------|-----|------|
| checkUsername | GET | /api/member/check-username?username={} | 이메일 중복 확인 |
| checkNickname | GET | /api/member/check-nickname?nickname={} | 닉네임 중복 확인 |
| getLoginStatus | GET | /api/member/login-status | 로그인 여부 확인 |
| deleteUser | POST | /api/member/delete-user | 회원 탈퇴 |
| updateUser | POST | /api/member/update-user | 회원 정보 수정 |
| save | POST | /api/member/save | 회원가입 처리 |
| setFollow | POST | /api/follow | 팔로우 등록 |
| setUnfollow | POST | /api/unfollow | 팔로우 취소 |
| getFollowerList | GET | /api/followers?user={id} | 팔로워 목록 조회 |
| getFollowingList | GET | /api/following?user={id} | 팔로잉 목록 조회 |

</details>

<details>
<summary>📝 게시글 API</summary>

| 메서드명 | HTTP | URL | 설명 |
|----------|------|-----|------|
| savePost | POST | /api/board/{boardId}/post | 게시글 작성 |
| findAllPosts | GET | /api/post | 전체 게시글 조회 |
| deletePost | DELETE | /api/post/{id} | 게시글 삭제 |
| updatePost | PUT | /api/post/{id} | 게시글 수정 |
| getPost | GET | /api/post/{id} | 게시글 상세 조회 |
| findPostsByBoard | GET | /api/board/{boardId}/posts | 게시판별 게시글 목록 |
| searchPosts | GET | /posts/search | 키워드 검색 (옵션: 게시판 필터) |
| uploadImage | POST | /api/image/upload | S3 이미지 업로드 |
| toggleLike | POST | /api/posts/{id}/like | 좋아요 토글 |
| toggleHidePost | PATCH | /api/post/{id}/hide | 게시글 숨김/해제 |
| unhidePost | PATCH | /api/post/{id}/unhide | 숨김 해제 |

</details>

<details>
<summary>💬 댓글 API</summary>

| 메서드명 | HTTP | URL | 설명 |
|----------|------|-----|------|
| saveComment | POST | /api/post/{postId}/comments | 댓글 작성 |
| saveReply | POST | /api/comments/{parentCommentId}/replies | 대댓글 작성 |
| findComment | GET | /api/comments/{commentId} | 댓글 조회 |
| updateComment | PUT | /api/comments/{commentId} | 댓글 수정 |
| deleteComment | DELETE | /api/comments/{commentId} | 댓글 삭제 |
| findCommentsByPostId | GET | /api/articles/{articleId}/comments | 게시글 댓글 목록 조회 |

</details>

<details>
<summary>✉️ 쪽지 API</summary>

| 메서드명 | HTTP | URL | 설명 |
|----------|------|-----|------|
| send | POST | /api/note | 쪽지 전송 |
| received | GET | /api/note/received | 받은 쪽지 목록 |
| sent | GET | /api/note/sent | 보낸 쪽지 목록 |
| delete | DELETE | /api/note/{id} | 쪽지 삭제 |
| hasUnReadNote | GET | /api/note/has-unread | 안 읽은 쪽지 확인 |

</details>



## 🎥 동작 영상
🔗 [유튜브 이동](https://youtu.be/PpPM_HvJ6uo)



## 📘 Notion 문서

🔗 [노션 문서 바로 가기](https://www.notion.so/oreumi/2-RUNTIME-1dcebaa8982b8016aca3def891911610)
