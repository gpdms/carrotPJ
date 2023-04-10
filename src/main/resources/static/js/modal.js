 //------리뷰 삭제 모달-----//
  const body = document.querySelector("body");
  const reviewModal= document.querySelector(".reviewModal");
  const rvModalBtn = document.getElementById("review_modal_btn");
  const rvModalBtn2 = document.getElementById("review_modal_btn2");
  const rvCloseBtn = document.getElementById("review_close_btn");

  function modalOpen() {
    reviewModal.style.display = "block";
    body.style.overflow = "hidden";
  }
  function modalClose() {
    reviewModal.style.display = "none";
    body.style.overflow = "auto";
  }

  // 클릭시 모달오픈
  rvModalBtn.addEventListener('click', ()=> {
    modalOpen()
  });
  rvModalBtn2.addEventListener('click', modalOpen);
  // 외부 클릭시 모달창닫음
  window.addEventListener('click', (e) => {
    e.target === reviewModal ? modalClose() : false
  })
  //클릭시 창닫음
  rvCloseBtn.addEventListener('click', modalClose);


