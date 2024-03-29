let reviewStateCode = '0';

function createMessage(reviewStateNum) {
    if(reviewStateNum == '0') {
        return `<p class="dot">&#183;&nbsp;&nbsp;</p><p class="form-title">아쉬웠던 점을 망고마켓 팀에 알려주세요.</p>
                    <p class="message-explanation">상대방에게 전달되지 않으니 안심하세요 </p>
                    <textarea class="message-area" name="message" placeholder="여기에 적어주세요. (선택사항)"></textarea>`;
    }else{
        return `<p class="dot">&#183;&nbsp;&nbsp;</p><p class="form-title">따뜻한 거래경험을 알려주세요! &#128155;</p>
                    <p class="message-explanation" >남겨주신 거래후기는 상대방의 프로필에 공개돼요</p>
                    <textarea  class="message-area" name="message"  placeholder="여기에 적어주세요. (선택사항)"></textarea>`;
    }
}
function viewSellerForm(reviewStateNum) {
    $("input:checkbox[name='indicator']").prop("checked", false);
    let message = createMessage(reviewStateNum);
    $(".form-content").addClass('content-border');
    if(reviewStateNum == '0') {
        reviewStateCode = '0';
        $("#negativeSellerIndicator").css("display", "block");
        $("#positiveSellerIndicator").css("display", "none");
        $("#reviewSellerMessage").empty();
        $("#reviewSellerMessage").append(message);
    } else {
        reviewStateCode = reviewStateNum=='1' ? '1' : '2';
        $("#negativeSellerIndicator").css("display", "none");
        $("#positiveSellerIndicator").css("display", "block");
        $("#reviewSellerMessage").empty();
        $("#reviewSellerMessage").append(message);
    }
}
function viewBuyerForm(reviewStateNum) {
    $("input:checkbox[name='indicator']").prop("checked", false);
    let message = createMessage(reviewStateNum);
    $(".form-content").addClass('content-border');
    if(reviewStateNum == '0') {
        reviewStateCode = '0';
        $("#negativeBuyerIndicator").css("display", "block");
        $("#positiveBuyerIndicator").css("display", "none");
        $("#reviewBuyerMessage").empty();
        $("#reviewBuyerMessage").append(message);
    } else {
        reviewStateCode = reviewStateNum == '1' ? '1' : '2';
        $("#negativeBuyerIndicator").css("display", "none");
        $("#positiveBuyerIndicator").css("display", "block");
        $("#reviewBuyerMessage").empty();
        $("#reviewBuyerMessage").append(message);
    }
}
function isCheckedBox() {
    if ($('input:checkbox[name="indicator"]:checked').length == 0) {
        alert("최소 1개 이상의 지표에 체크해주세요.");
        throw new Error();
    }
}
function getCheckedList() {
    let checkedArr = new Array();
    $('input:checkbox[name="indicator"]').each(function() {
        if(this.checked){//checked 처리된 항목의 값
            checkedArr.push(this.value);
        }
    });
    return checkedArr;
}
function formToJson(formData, checkedList) {
    return JSON.stringify({
        sellerId: formData.get("sellerId"),
        buyerId: formData.get("buyerId"),
        postId: formData.get("postId"),
        reviewStateCode : formData.get("reviewStateCode"),
        indicators: checkedList,
        message : formData.get("message")
    })
}
function reviewSeller() {
    isCheckedBox();
    let formData =  new FormData($("#reviewSellerForm")[0]);
    formData.delete("indicator");
    formData.append("reviewStateCode", reviewStateCode);
    let jsonData = formToJson(formData, getCheckedList())
    $.ajax({
        type: 'post',
        url: '/reviews/seller',
        data: jsonData,
        contentType: "application/json",
        success: function (data) {
            alert("리뷰가 등록되었습니다.");
            window.location.href= `/reviews/seller/${data}`
        },
        error: function (jqXHR) {
            let err = jqXHR.responseJSON;
            if(err.divisionCode == 'R001') {
                alert("이미 존재하는 리뷰입니다.");
                window.location.href= `/members/${buyerMe}/trade/buy-list`
            } else {
                alert("리뷰를 등록할 수 없습니다.");
            }
        }
    })
}

function reviewBuyer() {
    isCheckedBox();
    let formData =  new FormData($("#reviewBuyerForm")[0]);
    formData.delete("indicator");
    formData.append("reviewStateCode", reviewStateCode);
    let jsonData = formToJson(formData, getCheckedList())
    $.ajax({
        type: 'post',
        url: '/reviews/buyer',
        data: jsonData,
        contentType: "application/json",
        success: function (data) {
            alert("리뷰가 등록되었습니다.");
            window.location.href= `/reviews/buyer/${data}`
        },
        error: function (jqXHR) {
            let err = jqXHR.responseJSON;
            if(err.divisionCode == 'R001') {
                alert("이미 존재하는 리뷰입니다.");
                window.location.href= `/members/${sellerMe}/trade/sell-list`
            } else {
                alert("리뷰를 등록할 수 없습니다.");
            }
        }
    })
}

const reviewStateBtns = document.querySelectorAll('.reviewState-btn');
reviewStateBtns.forEach((btn) => {
    btn.addEventListener('click', () => {
        // 모든 버튼에서 'selected' 클래스 제거
        reviewStateBtns.forEach((btn) => {
            btn.classList.remove('selected');
        });
        // 클릭한 버튼에 'selected' 클래스 추가
        btn.classList.add('selected');
    });
});