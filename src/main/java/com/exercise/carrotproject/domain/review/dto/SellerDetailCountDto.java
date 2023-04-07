package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;


@Setter
@Getter
@ToString
public class SellerDetailCountDto {
    private ReviewSellerIndicator reviewSellerIndicator;
    private Long count;
    @QueryProjection
    public SellerDetailCountDto(ReviewSellerIndicator reviewSellerIndicator, Long count) {
        this.reviewSellerIndicator = reviewSellerIndicator;
        this.count = count;
    }
}
