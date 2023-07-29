package com.exercise.carrotproject.web.review.response;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import lombok.*;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
public class ReviewResponse {
    //공통
    private String postTitle;
    private String sellerId;
    private String buyerId;
    private String message;
    private ReviewState reviewState;
    List<ReviewIndicator> indicatorList;

    /**
     * 둘중 하나의 Id는 null
     * null이 아닐 때, 해당 리뷰가 존재
     * reviewSellerId - 판매자에 대한 리뷰
     * reviewBuyerId - 구매자에 대한 리뷰
     */
    private Long reviewSellerId;
    private Long reviewBuyerId;

    public static ReviewResponse of(final ReviewBuyer reviewBuyer) {
        final Post post = reviewBuyer.getPost();
        final Member buyer = reviewBuyer.getBuyer();
        final Member seller = reviewBuyer.getSeller();
        List<ReviewBuyerIndicator> buyerIndicators = reviewBuyer.getReviewBuyerIndicatorList();
        return ReviewResponse.builder()
                .postTitle(post.getTitle())
                .reviewState(reviewBuyer.getReviewState())
                .buyerId(buyer.getMemId())
                .sellerId(seller.getMemId())
                .indicatorList(ReviewIndicator.fromBuyerIndicatorList(buyerIndicators))
                .message(reviewBuyer.getMessage())
                .reviewBuyerId(reviewBuyer.getReviewBuyerId())
                .build();
    }

    public static ReviewResponse of(final ReviewSeller reviewSeller) {
        final Post post = reviewSeller.getPost();
        final Member buyer = reviewSeller.getBuyer();
        final Member seller = reviewSeller.getSeller();
        List<ReviewSellerIndicator> sellerIndicators = reviewSeller.getReviewSellerIndicatorList();
        return ReviewResponse.builder()
                .postTitle(post.getTitle())
                .reviewState(reviewSeller.getReviewState())
                .buyerId(buyer.getMemId())
                .sellerId(seller.getMemId())
                .reviewSellerId(reviewSeller.getReviewSellerId())
                .indicatorList(ReviewIndicator.fromSellerIndicatorList(sellerIndicators))
                .message(reviewSeller.getMessage())
                .build();
    }

}
