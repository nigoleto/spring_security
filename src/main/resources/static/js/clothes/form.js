document.getElementById('submitForm').addEventListener('click', async (e) => {
    e.preventDefault();
    const form = document.getElementById('clothesForm');
    const formData = new FormData(form);

    // address 도로명 주소만 가져오기
    const addressElement = document.getElementById('address');
    const fullAddress = addressElement.textContent;
    const roadAddress = fullAddress.replace(/.*?\s.*?\s/, "");
    const finalAddress = roadAddress.replace(/로(?=\d)/g, '로 ');

    // bin 엔티티 id 가져오기
    const binId = document.getElementById("bin-id").textContent;
    console.log("id: " + binId);

    // 토큰 가져오기
    const token = localStorage.getItem("token");

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

    // FormData 내용 출력
    for (let [key, value] of formData.entries()) {
        console.log(key, value); // key와 value 출력
    }

    try {
        const response = await fetch('/api/clothes', {
            method: 'POST',
            headers: {
                "Authorization": `${token}`
            },
            body: formData
        });

        if (response.ok) {
            window.location.href = "/";
        } else {
            console.error("파일 업로드 실패");
        }
    } catch (err) {
        alert('서버와 통신 중 오류가 발생했습니다.');
        console.error(err);
    }
});