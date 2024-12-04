document.addEventListener("DOMContentLoaded", function () {
    const token = localStorage.getItem("token"); // 로컬 스토리지의 토큰 가져오기

    if(token) {
        document.getElementById("logout").style.display = "block";
        document.getElementById("login").style.display = "none";
    } else {
        document.getElementById("logout").style.display = "none";
        document.getElementById("login").style.display = "block";
    }
})