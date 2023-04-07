package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewCommonIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@AllArgsConstructor
public class CommonDetailCountDto {
    private ReviewBuyerIndicator reviewBuyerIndicator;
    private Long sum;
}
