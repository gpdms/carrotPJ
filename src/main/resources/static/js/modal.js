 //------리뷰 숨김, 삭제 모달-----//
  const body = document.querySelector("body");
  const reviewModal= document.querySelector(".reviewModal");
  const rvModalBtn = document.querySelectorAll(".review_modal_btn");
  const rvCloseBtn = document.getElementById("review_close_btn");

  let reviewWho = null;
  let reviewId = 0;
  function modalOpen() {
    reviewWho = this.getAttribute('data-reviewWho');
    reviewId = this.getAttribute('data-reviewId');
    console.log(reviewWho);
    console.log(reviewId);
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


