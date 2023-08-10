    //탭전환 버튼
    const allReviewBtn = document.getElementById("allReview_btn");
    const buyerReviewBtn =document.getElementById("buyerReview_btn");
    const sellerReviewBtn =document.getElementById("sellerReview_btn");
    //탭전환 내용
    const allReviews = document.getElementById("allReviews");
    const buyerReviews = document.getElementById("buyerReviews");
    const sellerReviews = document.getElementById("sellerReviews");

    function allReview() {
        allReviews.style.display = "block";
        buyerReviews.style.display = "none";
        sellerReviews.style.display = "none";
        allReviewBtn.classList.add('is-current');
        buyerReviewBtn.classList.remove('is-current');
        sellerReviewBtn.classList.remove('is-current');
    }
    function sellerReview() {
        allReviews.style.display = "none";
        buyerReviews.style.display = "none";
        sellerReviews.style.display = "block";
        allReviewBtn.classList.remove('is-current');
        buyerReviewBtn.classList.remove('is-current');
        sellerReviewBtn.classList.add('is-current');
    }
    function buyerReview() {
        allReviews.style.display = "none";
        buyerReviews.style.display = "block";
        sellerReviews.style.display = "none";
        allReviewBtn.classList.remove('is-current');
        buyerReviewBtn.classList.add('is-current');
        sellerReviewBtn.classList.remove('is-current');
    }

    allReviewBtn.addEventListener('click', allReview);
    sellerReviewBtn.addEventListener('click', sellerReview);
    buyerReviewBtn.addEventListener('click', buyerReview);

    const reviewBars = document.querySelectorAll('.review-bars .review-bar');
    const reviewUnderline = document.querySelector('.review-underline');
    let currentIndex = 0;
    reviewBars.forEach((reviewBar, index) => {
        reviewBar.addEventListener('click', () => {
            if (currentIndex === index) {
                // 이미 선택된 요소를 다시 클릭한 경우 이전 위치로 이동
                reviewUnderline.style.left = index * parseFloat(getComputedStyle(reviewBar).width) + 'px';
                currentIndex = -1;
            } else {
                // 새로운 요소를 클릭한 경우 선택된 요소로 이동
                reviewUnderline.style.left = index * parseFloat(getComputedStyle(reviewBar).width) + 'px';
                currentIndex = index;
            }
        });
    });

    let review = {
        reviewedWho : null,
        reviewId : null
    };
    allReviews.addEventListener('click', function(event) {
            let target = event.target;
            if(event.target.id == 'all_more_btn') {
                allReviewMore()
            } else if (event.target.classList.contains('fa-ellipsis-vertical')) {
                modalOpen();
                target = event.target.parentNode;
            } else if (event.target.classList.contains('review_modal_btn') ) {
                modalOpen();
            }
            review = {
                reviewedWho: target.getAttribute('data-reviewedWho'),
                reviewId: target.getAttribute('data-reviewId')
            };
    });

    buyerReviews.addEventListener('click', function(event) {
        let target = event.target;
        if(target.id == 'buyer_more_btn') {
            buyerReviewMore()
        } else if (target.classList.contains('fa-ellipsis-vertical')) {
            modalOpen();
        target = event.target.parentNode;
        } else if (target.classList.contains('review_modal_btn') ) {
            modalOpen();
        }
        review = {
            reviewedWho: target.getAttribute('data-reviewedWho'),
            reviewId: target.getAttribute('data-reviewId')
        };
    });

    sellerReviews.addEventListener('click', function(event) {
        let target = event.target;
        if(target.id == 'seller_more_btn') {
            sellerReviewMore()
        } else if (target.classList.contains('fa-ellipsis-vertical')) {
            modalOpen();
            target = event.target.parentNode;
        } else if (target.classList.contains('review_modal_btn') ) {
            modalOpen();
        }
        review = {
            reviewedWho: target.getAttribute('data-reviewedWho'),
            reviewId: target.getAttribute('data-reviewId')
        };
    });

    function allReviewMore() {
        let cursorTime = $('#all_more_btn').attr('data-cursorTime');
        $.ajax({
            type: 'get',
            url: `/reviews/${memId}/messages?cursor=${cursorTime}`,
            success: function (data) {
                addAllReviewHtml(data);
            },
            error: function (err) {
                console.err(err);
            }
        })
    }
    function buyerReviewMore() {
        let buyerCursorId = $('#buyer_more_btn').attr('data-buyer-cursorId');
        $.ajax({
            type: 'get',
            url: `/reviews/${memId}/messages/buyer?cursor=${buyerCursorId}`,
            success: function (data) {
                addBuyerReviewHtml(data);
            },
            error: function (err) {
                console.log(err);
            }
        })
    }
    function sellerReviewMore() {
        let sellerCursorId =  $('#seller_more_btn').attr('data-seller-cursorId');
            $.ajax({
                type: 'get',
                url: `/reviews/${memId}/messages/seller?cursor=${sellerCursorId}`,
                success: function (data) {
                    addSellerReviewHtml(data);
                },
                error: function (err) {
                    console.log(err);
                }
            })
    }

    function addAllReviewHtml(cursorResult) {
        let reviewList = cursorResult.values;
        let isReviewed = loginMemId == memId;
        $('#allReviews').append('<hr/>');
        reviewList.forEach(function (review, index){
            let senderId = review.sender.memId;
            let isReviewer = loginMemId == senderId;
            let reviewerType = review.senderType == 'seller' ? "판매자" : "구매자";
            let locName = getLocName(review.sender.loc);
            let reviewOneHtml = `<div class="message-one">
                            <div class="profile-line">
                            <div class="message-profile-img">
                                <a href="/home/${review.sender.memId}">
                                    <img src="/members/${review.sender.memId}/profileImg" alt="프로필이미지">
                                </a>
                            </div>
                            <div class="message-info">
                                <div>
                                    <a href="/home/${review.sender.memId})"
                                        class="nickname">${review.sender.nickname}</a>
                                    <p class="loc"> ${locName} &#183; ${reviewerType}</p>
                                </div>
                            </div>
                        </div>
                        <div class="content-line">
                            <p>${review.message}</p>
                    </div>
                    <div class="time-line">
                        <p>${review.calculatedTimeForView}</p>
                    </div>`;

            if(review.senderType == 'seller') {
                if(isReviewed || isReviewer) {
                    reviewOneHtml = reviewOneHtml
                                        + `<div class="setting-bar">
                                                <a class="review_modal_btn"
                                                   data-reviewedWho="seller"
                                                   data-reviewId="${review.reviewId}">
                                                    <i class="fa-solid fa-ellipsis-vertical fa-xs"></i>
                                                </a>
                                            </div>
                                    </div><!--each Review-->`
                } else {
                    reviewOneHtml = reviewOneHtml + `</div><!--each Review-->`
                }
            } else if(review.senderType == 'buyer') {
                if(isReviewed || isReviewer) {
                    reviewOneHtml = reviewOneHtml
                                        + `<div class="setting-bar">
                                                    <div>
                                                    <a class="review_modal_btn"
                                                       data-reviewedWho="seller"
                                                       data-reviewId="${review.reviewId}">
                                                       <i class="fa-solid fa-ellipsis-vertical fa-xs"></i>
                                                    </a>
                                                    </div>
                                            </div>
                                        </div><!--each Review-->`
                } else {
                    reviewOneHtml = reviewOneHtml + `</div><!--each Review-->`
                }
            }
            $('#allReviews').append(reviewOneHtml);
        }); //each

        $('#all_more_btn').remove();
        if(cursorResult.hasNext) {
            const lastMessage = reviewList[reviewList.length-1]
            const btnElement = document.createElement("button");
            btnElement.id = 'all_more_btn'
            btnElement.className ='btn btn-outline-secondary btn-block col-12 mx-auto';
            btnElement.textContent = '더 보기';
            btnElement.setAttribute('data-cursorTime', lastMessage.createdTimeToString);
            allReviews.appendChild(btnElement);
        }
    }

    function addBuyerReviewHtml(cursorResult) {
        let reviewList = cursorResult.values;
        let isReviewed = loginMemId == memId;
        $('#buyerReviews').append('<hr/>');
        reviewList.forEach(function (review, index){
            let senderId = review.sender.memId;
            let isReviewer = loginMemId == senderId;
            let locName = getLocName(review.sender.loc)
            let reviewOneHtml = `<div class="message-one">
                    <div class="profile-line">
                        <div class="message-profile-img">
                            <a href="/home/${review.sender.memId}">
                                <img src="/members/${review.sender.memId}/profileImg" alt="프로필이미지">
                            </a>
                        </div>
                        <div class="message-info">
                            <div>
                                <a href="/home/${review.sender.memId})"
                                    class="nickname">${review.sender.nickname}</a>
                                <p class="loc"> ${locName} &#183; 판매자</p>
                            </div>
                        </div>
                    </div>
                    <div class="content-line">
                        <p>${review.message}</p>
                </div>
                <div class="time-line">
                    <p>${review.calculatedTimeForView}</p>
                </div>`

        if(isReviewed || isReviewer) {
        reviewOneHtml = reviewOneHtml
                            + `<div class="setting-bar">
                                    <a class="review_modal_btn"
                                       data-reviewedWho="buyer"
                                       data-reviewId="${review.reviewId}">
                                        <i class="fa-solid fa-ellipsis-vertical fa-xs"></i>
                                    </a>
                                </div>
                        </div><!--each Review-->`
        } else {
            reviewOneHtml = reviewOneHtml + `</div><!--each Review-->`
        }
            $('#buyerReviews').append(reviewOneHtml);
        }); //each

        $('#buyer_more_btn').remove();
        if(cursorResult.hasNext) {
            const lastMessage = reviewList[reviewList.length-1]
            const btnElement = document.createElement("button");
            btnElement.id = 'buyer_more_btn'
            btnElement.className ='btn btn-outline-secondary btn-block col-12 mx-auto';
            btnElement.textContent = '더 보기';
            btnElement.setAttribute('data-buyer-cursorId', lastMessage.reviewId);
            buyerReviews.appendChild(btnElement);
        }
    }

    function addSellerReviewHtml(cursorResult) {
        let reviewList = cursorResult.values;
        let isReviewed = loginMemId == memId;
        $('#sellerReviews').append('<hr/>');
        reviewList.forEach(function (review, index){
            let senderId = review.sender.memId;
            let isReviewer = loginMemId == senderId;
            let locName = getLocName(review.sender.loc)
            let reviewOneHtml = `<div class="message-one">
                    <div class="profile-line">
                        <div class="message-profile-img">
                            <a href="/home/${review.sender.memId}">
                                <img src="/members/${review.sender.memId}/profileImg" alt="프로필이미지">
                            </a>
                        </div>
                        <div class="message-info">
                            <div>
                                <a href="/home/${review.sender.memId})"
                                    class="nickname">${review.sender.nickname}</a>
                                <p class="loc"> ${locName} &#183; 구매자</p>
                            </div>
                        </div>
                    </div>
                    <div class="content-line">
                        <p>${review.message}</p>
                </div>
                <div class="time-line">
                    <p>${review.calculatedTimeForView}</p>
                </div>`

        if(isReviewed || isReviewer) {
            reviewOneHtml = reviewOneHtml
                         + `<div class="setting-bar">
                                <a class="review_modal_btn"
                                   data-reviewedWho="seller"
                                   data-reviewId="${review.reviewId}">
                                    <i class="fa-solid fa-ellipsis-vertical fa-xs"></i>
                                </a>
                            </div>
                        </div><!--each Review-->`
        } else {
            reviewOneHtml = reviewOneHtml + `</div><!--each Review-->`
        }
            $('#sellerReviews').append(reviewOneHtml);
        }); //each

        $('#seller_more_btn').remove();
        if(cursorResult.hasNext) {
            const lastMessage = reviewList[reviewList.length-1]
            const btnElement = document.createElement("button");
            btnElement.id = 'seller_more_btn'
            btnElement.className ='btn btn-outline-secondary btn-block col-12 mx-auto';
            btnElement.textContent = '더 보기';
            btnElement.setAttribute('data-seller-cursorId', lastMessage.reviewId);
            sellerReviews.appendChild(btnElement);
        }
    }

    function getLocName(loc) {
        switch(loc){
            case 'GANGBUK' : return '강북';
            case 'GANGNAM' : return '강남';
            case 'GANGDONG' : return '강동';
            case 'GANGSEO' : return '강서';
            default : console.warn('없는 지역 값입니다.');
            break;
        }
    }
