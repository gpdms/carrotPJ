package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.Map;

@Setter
@Getter
@ToString
public class BuyerDetailCountDto {
    private ReviewBuyerIndicator reviewBuyerIndicator;
    private Long count;

    @QueryProjection
    public BuyerDetailCountDto(ReviewBuyerIndicator reviewBuyerIndicator, Long count) {
        this.reviewBuyerIndicator = reviewBuyerIndicator;
        this.count = count;
    }
}
