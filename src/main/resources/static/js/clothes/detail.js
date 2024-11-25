document.addEventListener("DOMContentLoaded", function() {
    const clothesId = window.location.pathname.split("/").pop(); // URL에서 ID를 추출
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

    const edit = document.getElementById("edit");
    edit.addEventListener("click", function() {
        window.location.href = `/clothes/${clothesId}/edit`;
    });
});