 //------리뷰 숨김, 삭제 모달-----//
  const body = document.querySelector("body");
  const reviewModal= document.querySelector(".reviewModal");
  const rvModalBtn = document.querySelectorAll(".review_modal_btn");
  const rvCloseBtn = document.getElementById("review_close_btn");

  let reviewedWho = null;
  let reviewId = 0;
  function modalOpen() {
    reviewedWho = this.getAttribute('data-reviewedWho');
    reviewId = this.getAttribute('data-reviewId');
    reviewModal.style.display = "block";
    body.style.overflow = "hidden";
  }
  function modalClose() {
    reviewModal.style.display = "none";
    body.style.overflow = "auto";
  }
  // 클릭시 모달오픈
 rvModalBtn.forEach(function(button) {
     button.addEventListener('click', modalOpen);
 });
  // 외부 클릭시 모달창닫음
  window.addEventListener('click', (e) => {
    e.target === reviewModal ? modalClose() : false
  })
  //클릭시 창닫음
  rvCloseBtn.addEventListener('click', modalClose);


 //------리뷰 숨김, 보기, 삭제 기능-----//
 const detailBtn = document.getElementById("review_detail_btn");
 detailBtn.addEventListener('click', () => {
     if(reviewedWho=="seller") {
         location.href=`/reviews/seller/${reviewId}`;
     } else if(reviewedWho=="buyer") {
         location.href=`/reviews/buyer/${reviewId}`;
     }
 })

 const hideBtn = document.getElementById("review_hide_btn");
 if(hideBtn) {
     hideBtn.addEventListener('click', hideReview);
 }
 const delBtn = document.getElementById("review_del_btn");
 if(delBtn) {
     delBtn.addEventListener('click', deleteReview);
 }
 function hideReview() {
     if (!hideConfirm()) {
         return;
     }
     if(reviewedWho=="seller") {
         $.ajax({
             type: 'post',
             url: '/reviews/seller/hide',
             data: {reviewSellerId : reviewId},
             success: function (data) {
                 alert("숨김에 성공했습니다");
                 location.reload();
             },
             error: function (err) {
                 console.log(err);
                 alert("리뷰를 숨길 수 없습니다.");
             }
         })
     } else if (reviewedWho=="buyer"){
         $.ajax({
             type: 'post',
             url: '/reviews/buyer/hide',
             data:  {reviewBuyerId : reviewId},
             success: function (data) {
                 alert("숨김에 성공했습니다.");
                 location.reload();
             },
             error: function (err) {
                 console.log(err);
                 alert("리뷰를 숨길 수 없습니다.");
             }
         })
     }
 }
 function deleteReview() {
     if (!deleteConfirm()) {
         return;
     }
     if (reviewedWho == "seller") {
         $.ajax({
             type: 'delete',
             url: '/reviews/seller/' + reviewId,
             success: function (data) {
                 alert("삭제에 성공했습니다");
                 location.reload();
             },
             error: function (err) {
                 console.log(err);
                 alert("리뷰를 삭제할 수 없습니다");
             }
         })
     } else if (reviewedWho == "buyer") {
         $.ajax({
             type: 'delete',
             url: '/reviews/buyer/' + reviewId,
             success: function (data) {
                 alert("삭제에 성공했습니다");
                 location.reload();
             },
             error: function (err) {
                 console.log(err);
                 alert("리뷰를 삭제할 수 없습니다");
             }
         })
     }
 }

 function hideConfirm() {
     return confirm("거래 후기를 숨기시겠습니까? 숨기면 다시 되돌릴 수 없습니다.");
 }
 function deleteConfirm() {
     return confirm("거래 후기를 삭제하시겠습니까?");
 }

