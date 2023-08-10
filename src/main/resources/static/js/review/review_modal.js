 //------리뷰 숨김, 삭제 모달-----//
  const body = document.querySelector("body");
  const reviewModal= document.querySelector(".reviewModal");
  // const rvModalBtn = document.querySelectorAll(".review_modal_btn");
  const rvCloseBtn = document.getElementById("review_close_btn");;

  function modalOpen() {
    reviewModal.style.display = "block";
    body.style.overflow = "hidden";
  }
  function modalClose() {
    reviewModal.style.display = "none";
    body.style.overflow = "auto";
  }

  // 외부 클릭시 모달창닫음
  window.addEventListener('click', (e) => {
    e.target === reviewModal ? modalClose() : false
  })
  //클릭시 창닫음
  rvCloseBtn.addEventListener('click', modalClose);

 //------리뷰 숨김, 보기, 삭제 기능-----//
 const detailBtn = document.getElementById("review_detail_btn");
 const hideBtn = document.getElementById("review_hide_btn");
 const delBtn = document.getElementById("review_del_btn");

 if(hideBtn){
     hideBtn.addEventListener('click', function() {
         hideReview(review);
     });
 }
 if(delBtn){
     delBtn.addEventListener('click', function() {
         deleteReview(review);
     });
 }
 if(detailBtn){
     detailBtn.addEventListener('click', function() {
         detailReview(review);
     });
 }

 function detailReview(review) {
     if(review.reviewedWho=="seller") {
         location.href=`/reviews/seller/${review.reviewId}`;
     } else if(review.reviewedWho=="buyer") {
         location.href=`/reviews/buyer/${review.reviewId}`;
     }
 }

 function hideReview(review) {
     if (!hideConfirm()) {
         return;
     }
     if(review.reviewedWho=="seller") {
         $.ajax({
             type: 'post',
             url: '/reviews/seller/hide',
             data: {reviewSellerId : review.reviewId},
             success: function (data) {
                 alert("숨김에 성공했습니다");
                 location.reload();
             },
             error: function (err) {
                 console.log(err);
                 alert("리뷰를 숨길 수 없습니다.");
             }
         })
     } else if (review.reviewedWho=="buyer"){
         $.ajax({
             type: 'post',
             url: '/reviews/buyer/hide',
             data:  {reviewBuyerId : review.reviewId},
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

 function deleteReview(review) {
     if (!deleteConfirm()) {
         return;
     }
     if (review.reviewedWho == "seller") {
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
     } else if (review.reviewedWho == "buyer") {
         $.ajax({
             type: 'delete',
             url: '/reviews/buyer/' + review.reviewId,
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

