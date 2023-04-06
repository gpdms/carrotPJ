package com.exercise.carrotproject.web.review.form;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReviewDetailForm {
    private String postTitle;
    private ReviewState reviewState;

    private String sellerId;
    private String buyerId;
    private String message;

    private Long reviewSellerId;
    List<ReviewSellerIndicator> sellerIndicatorList;

    private Long reviewBuyerId;
    List<ReviewBuyerIndicator> buyerIndicatorList;
}
