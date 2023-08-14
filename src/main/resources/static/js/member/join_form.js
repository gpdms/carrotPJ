let isPassedEmail = false;
let isPassedId = false;
function activeSignupBtn () {
    const joinBtn = document.getElementById('join-btn');
    if (isPassedId && isPassedEmail) {
        joinBtn.disabled = false;
    } else if (~isPassedId || ~isPassedEmail) {
        joinBtn.disabled = true;
    }
}

//입력데이터 유효성 검증
const memIdInput = document.getElementById("memId");
const nicknameInput = document.getElementById("nickname");
const pwdInput = document.getElementById("pwd");
const pwdConfirmInput = document.getElementById("pwdConfirm");
const emailInput = document.getElementById("email");
const locSelect = document.getElementById("loc");

memIdInput.addEventListener("change", function(event) {
    let memId = memIdInput.value;
    if (isValidMemId(memId)) {
        isDuplicatedMemId(memId);
    }
});

function isValidMemId(memId) {
    const pattern = /^[a-zA-Z0-9]{6,12}$/;
    if (pattern.test(memId)) {
        isPassedId = true;
        activeJoinBtn();
        memIdInput.classList.remove('field-error-border');
        $('#memId-error').empty()
        return true;
    } else {
        isPassedId = false;
        activeJoinBtn();
        memIdInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="memId-error">아이디는 영문 대/소문자이나 숫자로 6~12자로 구성되어야합니다. </div>`
        $('#memId-error').replaceWith(str)
        return false;
    }
}

function isDuplicatedMemId(memId) {
    $.ajax({
        type: "GET",
        url: `join/memId/${memId}`
    }).done(function (data) {
        isPassedId = true;
        activeJoinBtn();
        memIdInput.classList.remove('field-error-border');
        $('#memId-error').empty();
    }).fail(function (err) {
        isPassedId = false;
        activeJoinBtn();
        memIdInput.classList.add('field-error-border');
        $('#memId-error').replaceWith(`<div class="field-error" id="memId-error">사용할 수 없는 아이디입니다.</div>`)
    })
}

emailInput.addEventListener("change", function(event) {
    let email = emailInput.value;
    if (isValidEmail(email)) {
        isDuplicatedEmail(email);
    }
});

function isValidEmail(email) {
    const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (emailPattern.test(email)) {
        isPassedEmail = false;
        activeJoinBtn();
        emailInput.classList.remove('field-error-border');
        $('#email-error').empty();
        return true;
    } else {
        isPassedEmail = false;
        activeJoinBtn();
        emailInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="email-error">이메일 형식에 맞지 않습니다.</div>`
        $('#email-error').replaceWith(str);
        return false;
    }
}

function isDuplicatedEmail(email) {
    $.ajax({
        type: "GET",
        url: `join/email/${email}`
    }).done(function (data) {
        emailInput.classList.remove('field-error-border');
        $('#email-error').empty();
        return false;
    }).fail(function (err) {
        isPassedEmail = false;
        activeJoinBtn();
        emailInput.classList.add('field-error-border');
        $('#email-error').replaceWith(`<div class="field-error" id="email-error">이미 사용중인 이메일입니다.</div>`);
        return true;
    })
}

nicknameInput.addEventListener("change", function(event) {
    const  pattern = /^[a-zA-Z0-9가-힣]{2,15}$/;
    let nick = nicknameInput.value;
    if (nick.length < 2 || nick.length > 15) {
        nicknameInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="nick-error">닉네임은 2~15자여야 합니다.</div>`
        $('#nick-error').replaceWith(str);
    } else
    if (!pattern.test(nick)) {
        nicknameInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="nick-error">사용할 수 없는 닉네임입니다.</div>`
        $('#nick-error').replaceWith(str);
    } else {
        nicknameInput.classList.remove('field-error-border');
        $('#nick-error').empty();
    }
});
pwdInput.addEventListener("change", function(event) {
    const pattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*\W).{8,16}$/;
    let pwd = pwdInput.value;
    if (pattern.test(pwd)) {
        pwdInput.classList.remove('field-error-border');
        $('#pwd-error').empty();
    } else {
        pwdInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="pwd-error">비밀번호는 8~16자의 영문, 특수문자, 숫자를 사용해야합니다.</div>`;
        $('#pwd-error').replaceWith(str);
    }
});
pwdConfirmInput.addEventListener("input", function(event) {
    let pwd = pwdInput.value;
    let pwdConfirm = pwdConfirmInput.value;
    if (pwd === pwdConfirm) {
        pwdConfirmInput.classList.remove('field-error-border');
        $('#pwdConfirm-error').empty();
    } else {
        pwdConfirmInput.classList.add('field-error-border');
        const str = `<div class="field-error" id="pwdConfirm-error">비밀번호가 일치하지 않습니다.</div>`;
        $('#pwdConfirm-error').replaceWith(str);
    }
});

//이메일 인증
$('#sendEmailCode').click(function() {
    let email = $('#email').val();
    if (isValidEmail(email) && !isDuplicatedEmail(email)) {
        sendEmailCode();
        $('#send-waiting').text("인증 이메일을 전송중입니다. 잠시만 기다려주세요.");
    }
})

function sendEmailCode() {
    $.ajax({
        type: "POST",
        url: "join/email/auth-code",
        contentType: "application/json; charset=utf-8",
        data: JSON.stringify({
            email : emailInput.value
        })
    }).done(function (authCode) {
        console.log(authCode)
        $('#send-waiting').empty();
        alert("해당 이메일로 인증번호 발송이 완료되었습니다.")
        const str = `<div class="input" id ="email-code-check">
                    <div class="form-group last mb-4 check_input">
                        <label for="emailConfirm" id="emailConfirmTxt">인증번호를 입력해주세요</label>
                        <input type="text" class="form-control" id="emailConfirm">
                    </div>
                </div>`
        $('#email-code-check').replaceWith(str);
        checkEmailCode(authCode, $('#emailConfirm'), $('#emailConfirmTxt'));
    }).fail(function (err) {
        $('#send-waiting').empty();
        const errResposne = err.responseJSON;
        console.error(errResposne);
    })
}

function checkEmailCode(authCode, emailConfirm, emailConfirmTxt){
    emailConfirm.on("keyup", function(){
        if (authCode != emailConfirm.val()) {
            emailConfirmTxt.html("<span id='checkTxt'>인증번호가 잘못되었습니다</span>")
            $("#checkTxt").css({
                "color" : "#FA3E3E",
                "font-weight" : "bold",
                "font-size" : "12px"
            })
            isPassedEmail = false;
            activeJoinBtn()
        } else {
            emailConfirmTxt.html("<span id='checkTxt'>인증번호 확인 완료</span>")
            $("#checkTxt").css({
                "color" : "#0D6EFD",
                "font-weight" : "bold",
                "font-size" : "12px"
            })
            isPassedEmail = true;
            activeJoinBtn()
        }
    })
}

//회원가입 버튼 클릭시
$('#joinForm').on('submit', function(e) {
    e.preventDefault();
    let url = 'join';
    const joinData = {
        memId : memIdInput.value,
        pwd : pwdInput.value,
        pwdConfirm : pwdConfirmInput.value,
        email : emailInput.value,
        nickname: nicknameInput.value,
        loc : locSelect.value
    };
    joinRequest(url, joinData);
})

function joinRequest(url, joinData) {
    $.ajax({
        type: 'post',
        url: 'join',
        contentType: "application/json; charset=utf-8",
        data:  JSON.stringify(joinData),
        success: function (data) {
            alert("회원가입에 성공했습니다. 로그인해주세요.");
            location.href="/login"
        },
        error: function (jqXHR) {
            alert("회원가입에 실패했습니다.");
            console.error(jqXHR.responseJSON);
        }
    })
}

function joinSocialMember(platform) {
    $.ajax({
        type: 'post',
        url: `members/join-social?platform=${platform}`,
        contentType: "application/json; charset=utf-8",
        data:  JSON.stringify({
            loc : $("select[name=locSocial]").val(),
            nickname : $('#nicknameSocial').val(),
            email : $('#emailSocial').val(),
            profImgUrl : $('#profImgUrl').val(),
        }),
        success: function (data) {
            alert("회원가입에 성공했습니다. 로그인해주세요");
            location.href="/login"
        },
        error: function (err) {
            const message = err.responseJSON;
            $('#nick-error-social').replaceWith(`<div id="nick-error-social" class="field-error">${message.nickname}</div>`)
            $("#nicknameSocial").addClass("field-error-border");
        }
    })
}