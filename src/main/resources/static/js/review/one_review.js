
const buyerDelBtn =document.getElementById('buyerDelBtn')
if(buyerDelBtn) {
    buyerDelBtn.addEventListener('click', deleteBuyer);
}

function deleteBuyer() {
    if (!delConfirm()) { return; }
    let url = '/reviews/buyer/' + reviewBuyerId
    fetch(url, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            return response;  // HTTP 응답 객체를 JSON으로 변환하여 반환
        } else {
            throw new Error('서버로부터 응답을 받지 못했습니다.');  // 예외 발생
        }
    }).then(data => {
        alert('삭제에 성공했습니다.');
        window.location.href = `/members/${sellerMe}/trade/sell-list`;
    }).catch(err => {
        alert('삭제에 실패했습니다.');
    })
}

const sellerDelBtn = document.getElementById('sellerDelBtn');
if(sellerDelBtn) {
    sellerDelBtn.addEventListener("click", deleteSeller);
}

function deleteSeller(){
    if (!delConfirm()) {
        return;
    }
    let url = '/reviews/seller/' + reviewSellerId
    fetch(url, {
        method: 'DELETE'
    }).then(response => {
        if (response.ok) {
            return response;
        } else {
            throw new Error('서버로부터 응답을 받지 못했습니다.');  // 예외 발생
        }
    }).then(data => {
        alert('삭제에 성공했습니다.');
        window.location.href = `/members/${buyerMe}/trade/buy-list`;
    }).catch(err => {
        alert('삭제에 실패했습니다.');
    });
}

function delConfirm() {
    return confirm('리뷰를 삭제하시겠습니까?');
}