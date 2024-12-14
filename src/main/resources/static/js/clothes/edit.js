let binId = "";
document.addEventListener("DOMContentLoaded", function() {
    const clothesId = window.location.pathname.split("/")[2]; // URL에서 ID를 추출
    const token = localStorage.getItem("token"); // 로컬 스토리지의 토큰 가져오기

    console.log(clothesId);
    fetchClothesDetails(clothesId);

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
            });
    }

    function displayClothesDetails(clothes) {
        document.getElementById("newTitle").value = clothes.title;
        document.getElementById("newContent").value = clothes.description;
        document.getElementById("gender").value = clothes.gender;
        document.getElementById("size").value = clothes.size;
        document.getElementById("status").value = clothes.status;
        binId = clothes.gwangju.id;

        // 첨부파일
        if (clothes.attachList) {
            const fileList = clothes.attachList;

            const attachBox = document.getElementById("attach-box");
            attachBox.innerHTML = fileList
                .map(file => `
                        <div id="attach">
                        <img src="${file.fileUrl}" alt="첨부 이미지">
                        <div> 
                            <span class="file-name">${file.fileName} </span>
                            <a href="#" class="attach-delete" data-filename="${file.fileName}" data-clothesid="${clothes.id}">삭제</a>
                        </div> 
                        </div>
                    `)
                .join("");

            // Event Delegation: attach-box의 삭제 링크만 이벤트 처리
            attachBox.addEventListener("click", async (e) => {
                const target = e.target;

                // 클릭한 요소가 <a class="attach-delete">인지 확인
                if (target.tagName === "A" && target.classList.contains("attach-delete")) {
                    e.preventDefault();

                    const fileName = target.dataset.filename;
                    const clothesId = target.dataset.clothesid;

                    if (window.confirm("해당 첨부파일이 해당 게시글에서 바로 삭제됩니다.")) {
                        // 첨부파일 삭제 API 호출
                        try {
                            const response = await fetch(`/api/attach?fileName=${fileName}&clothesId=${clothesId}`, {
                                method: 'DELETE',
                                headers: {
                                    "Authorization": `${token}`
                                }
                            });

                            if (response.ok) {
                                alert("첨부파일이 삭제되었습니다.");
                                fetchClothesDetails(clothesId);
                            } else {
                                console.error("첨부파일 삭제 실패");
                            }
                        } catch (err) {
                            alert('서버와 통신 중 오류가 발생했습니다.');
                            console.error(err);
                        }
                    }
                }
            });
        }

    }

    function displayClothesError(error) {
        const errorCode = error.match(/\d{3}/)?.[0]; // 3자리 숫자를 매칭

        if (errorCode === "404") {
            document.getElementById("layout").innerHTML = `
                <header th:replace="~{layout/header::header}"></header>
                <h3 class="error-404"> 삭제되었거나 존재하지 않는 게시글 입니다.</h3>
            `;
        }

        if (errorCode === "401") {
            document.getElementById("layout").innerHTML = `
                <header th:replace="~{layout/header::header}"></header>
                <h3 class="error-401">로그인 후 이용해 주세요.</h3>
                <a href="/login"> 로그인 하러 가기 </a>
            `;
        }
    }
});


document.getElementById('submitForm').addEventListener('click', async () => {
    const form = document.getElementById('clothesForm');
    const formData = new FormData(form);

    const clothesId = window.location.pathname.split("/")[2]; // URL에서 ID를 추출

    // 토큰 가져오기
    const token = localStorage.getItem("token");

    // 주소 가져오기
    const finalAddress = "editAddress";



    // Form에서 title과 content 값을 수집
    const title = document.getElementById('newTitle').value;
    const content = document.getElementById('newContent').value;
    const gender = document.getElementById("gender").value;
    const size = document.getElementById("size").value;
    const status = document.getElementById("status").value;

    // ClothesRequestDto JSON 데이터 추가
    const clothesRequestDto = {
        title: title,
        description: content,
        binId: binId,
        gender: gender,
        size: size,
        status: status
    };

    // JSON 데이터는 문자열로 변환해서 FormData에 추가
    formData.append('clothesRequestDto', new Blob([JSON.stringify(clothesRequestDto)], { type: 'application/json' }));

    // 파일 데이터 추가
    const fileInput = document.getElementById('file');
    if (fileInput.files.length > 0) {
        formData.append('file', fileInput.files[0]);
    }

    try {
        const response = await fetch(`/api/clothes/${clothesId}`, {
            method: 'PUT',
            headers: {
                "Authorization": `${token}`
            },
            body: formData
        });

        if (response.ok) {
            window.location.href = `/clothes/${clothesId}`;
        } else {
            console.error("파일 업로드 실패");
        }
    } catch (err) {
        alert('서버와 통신 중 오류가 발생했습니다.');
        console.error(err);
    }
});
