//리뷰 모달
const rvModalBtn = document.querySelectorAll(".review_modal_btn");

let review = {
    reviewedWho : null,
    reviewId : null };

rvModalBtn.forEach(function(button) {
    button.addEventListener('click', modalOpen);
    review = {
        reviewedWho: button.getAttribute('data-reviewedWho'),
        reviewId: button.getAttribute('data-reviewId')
    };
});

//매너온도
const mannerDom = document.getElementsByClassName("manner")[0];
const mannerScoreDom =document.getElementsByClassName("mannerScore")[0];
mannerDom.style.width= `${mannerScore}%`;
if(mannerScore >= 85) {
    mannerScoreDom.style.color = '#e61919';
    mannerDom.style.backgroundColor = '#e61919';
} else if (mannerScore >= 50 && mannerScore < 85){
    mannerScoreDom.style.color = '#ff9d0a';
    mannerDom.style.backgroundColor = '#ff9d0a';
} else if (mannerScore >= 30 && mannerScore < 50) {
    mannerScoreDom.style.color = '#009900';
    mannerDom.style.backgroundColor = '#009900';
} else if(mannerScore < 30) {
    mannerScoreDom.style.color = '#0d6efd';
    mannerDom.style.backgroundColor = '#0d6efd';
}

$("#block_btn").on("click", block);
function block() {
    const blockConfirm = confirm("차단시 서로의 게시글을 확인하거나 채팅을 할 수 없어요. 차단하실래요?");
    if (!blockConfirm) {
        return;
    }
    requestBlock();
}
function requestBlock() {
    let data = {
        fromMemId : loginMemberId,
        toMemId : homeMemberId
    }
    $.ajax({
        type: 'post',
        url: '/blocks',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            alert("차단에 성공했습니다.");
            $('#postList').load(location.href+' #postList');
            $('#pf-setting').empty();
            $('#pf-setting').append( `<a id="block_cancel_btn" class="pf-btn" type="button">차단해제</a>`)
        },
        error: function (err) {
            console.err(err.responseJSON);
            alert("차단에 실패했습니다.");
        }
    })
}

$("#block_cancel_btn").on("click", cancelBlock);
function cancelBlock(){
    let data = {
        fromMemId : loginMemberId,
        toMemId : homeMemberId
    }
    $.ajax({
        type: 'delete',
        url: '/blocks',
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        success: function (data) {
            alert("차단을 해제했습니다");
            $('#postList').load(location.href+' #postList');
            $('#pf-setting').empty();
            $('#pf-setting').append( `<a id="block_btn" class="pf-btn" type="button">차단하기</a>`)
        },
        error: function (err) {
            alert("차단에 실패했습니다.");
        }
    })
}