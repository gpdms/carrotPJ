// const body = document.querySelector("body");
const loginMd = document.querySelector('.loginMd');

function openLoginModal() {
    if (loginMd) {
        loginMd.style.display = "block";
    } else { const modalElement = document.createElement('div');
        $.ajax({
            url: '/login',
            method: 'get',
            success: function (html) {
                modalElement.innerHTML = html;
                document.body.appendChild(modalElement);
            }
        })
    };
}
function loginModalClose() {
    loginMd.style.display = "none";
    console.log("클로즈?")
}
window.addEventListener('click', (e) => {
    console.log("클로즈??????")
    console.log(e.target === loginMd)
    e.target === loginMd ? loginModalClose() : false
})

function loginWithKakao() {
    $.ajax({
        url: '/login/getKakaoLoginURL',
        type: 'get',
    }).done(function (res) {
        location.href = res;
    });
}