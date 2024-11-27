document.addEventListener("DOMContentLoaded", function() {
    const clothesId = window.location.pathname.split("/").pop(); // URL에서 ID를 추출
    const token = localStorage.getItem("token"); // 로컬 스토리지의 토큰 가져오기
    fetchClothesDetails(clothesId);
    fetchComment();

    function fetchClothesDetails(id) {
        fetch(`/api/clothes/${id}`, {
            method: 'GET',
            headers: {
                "Authorization": `${token}`,
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                displayClothesDetails(data);
            })
            .catch(error => {
                console.error('Error fetching clothes details:', error);
                displayClothesError(error.message);
            });
    }

    function displayClothesDetails(clothes) {
        document.getElementById("clothes-title").textContent = clothes.title;
        document.getElementById("clothes-content").innerHTML = clothes.description.replace(/\n/g, "<br>");

        // 작성일을 원하는 형식으로 변환
        const date = new Date(clothes.createdAt);
        const formattedDate = date.toISOString().slice(0, 16).replace('T', ' ');
        document.getElementById("clothes-date").textContent = formattedDate;

        // 첨부파일
        if(clothes.attachList) {
            const fileList = clothes.attachList;

            const attachBox = document.getElementById("attach-box");
            attachBox.innerHTML = fileList
                .map(file => `
                    <div id="attach">
                        <img src="${file.fileUrl}" alt="첨부 이미지">
                    </div>
                `)
                .join("");
        }

        // 수정 삭제 버튼
        const payload = JSON.parse(atob(token.split(".")[1]));
        if(payload.sub === clothes.user.email) {
            document.getElementById("buttons").style.display = "block";
        }
    }

    function displayClothesError(error) {
        const errorCode = error.match(/\d{3}/)?.[0]; // 3자리 숫자를 매칭

        if(errorCode === "404") {
            document.getElementById("layout").innerHTML = `
            <header th:replace="~{layout/header::header}"></header>
            <h3 class="error-404"> 삭제되었거나 존재하지 않는 게시글 입니다.</h3>
        `;
        }

        if(errorCode === "401") {
            document.getElementById("layout").innerHTML = `
            <header th:replace="~{layout/header::header}"></header>
            <h3 class="error-401">로그인 후 이용해 주세요.</h3>
            <a href="/login"> 로그인 하러 가기 </a>
        `;
        }
    }

    // 게시글 수정
    const edit = document.getElementById("edit");
    edit.addEventListener("click", function() {
        window.location.href = `/clothes/${clothesId}/edit`;
    });
    // 게시글 삭제
    document.getElementById("delete").addEventListener("click", function() {

        if(window.confirm("해당 게시글을 삭제하시겠습니까?")) {

            fetch(`/api/clothes/${clothesId}`, {
                method: 'DELETE',
                headers: {
                    "Authorization": `${token}`,
                    'Content-Type': 'application/json',
                }
            })
                .then(response => {
                    if (response.ok) {
                        alert("삭제되었습니다.")
                        window.location.href = "/";
                    }
                })
                .catch(error => {
                    console.error('Error fetching clothes details');
                });
        }

    })

    // 댓글 조회
    function fetchComment() {
        fetch(`/api/comment/${clothesId}`, {
            method: 'GET',
            headers: {
                "Authorization": `${token}`,
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                displayComments(data);
            })
            .catch(error => {
                alert('Error fetching clothes details:' + error);
            });
    }
    function displayComments(comments) {
        const commentBox = document.getElementById("comment-list")

        comments.forEach(comment => {

            const date = new Date(comment.createdAt).toISOString().slice(2, 16).replace("T"," ");

            const payload = JSON.parse(atob(token.split(".")[1]));
            let isYourComment = payload.sub === comment.email? "block" : "none";

            const commentDiv = document.createElement("div");
            commentDiv.className = "comment";
            commentDiv.innerHTML = `
                        <div>
                            <span class="comment-nickname">${comment.nickname}</span>
                            <span class="comment-createdAt">${date}</span>
                        </div>
                        <div>
                            <button class="comment-delete" style="display: ${isYourComment};"> 삭제 </button>
                        </div>
                        
                        <p>${comment.content}</p>
            `;

            // 삭제 버튼 이벤트 리스너 추가
            const deleteButton = commentDiv.querySelector(".comment-delete");
            deleteButton.addEventListener("click", function () {
                if (window.confirm("해당 댓글을 삭제하시겠습니까?")) {
                    fetch(`/api/comment/${comment.id}`, {
                        method: "DELETE",
                        headers: {
                            "Authorization": `${token}`,
                            "Content-Type": "application/json",
                        },
                    })
                        .then(response => {
                            if (response.ok) {
                                alert("댓글이 삭제되었습니다.");
                                commentDiv.remove(); // 삭제된 댓글 DOM 요소 제거
                            } else {
                                alert("댓글 삭제에 실패했습니다.");
                            }
                        })
                        .catch(error => {
                            console.error("Error deleting comment:", error);
                        });
                }
            });


            commentBox.appendChild(commentDiv);
        })
    }

    // 댓글 생성
    const commentInput = document.getElementById("comment-input")
    document.getElementById("new-comment-box").addEventListener("submit", function(e) {
        e.preventDefault();
        console.log("댓글 생성 버튼은 눌러짐");
        const commentContent = commentInput.value;
        fetch(`/api/comment/${clothesId}`, {
            method: 'POST',
            headers: {
                "Authorization": `${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                "content" : `${commentContent}`
            })
        })
            .then(response => {
                if (response.ok) {
                    fetchComment();
                }
            })
            .catch(error => {
                console.error('Error fetching comment');
            });
    })

});