package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Getter
@Builder
@ToString
public class ReviewBuyerDetailDto {
    private Long reviewBuyerReviewId;
    private String reviewBuyerId;
    private String buyerId;
    private ReviewBuyerIndicator reviewBuyerIndicator;

    @QueryProjection
    public ReviewBuyerDetailDto(Long reviewBuyerReviewId, String reviewBuyerId, String buyerId, ReviewBuyerIndicator reviewBuyerIndicator) {
        this.reviewBuyerReviewId = reviewBuyerReviewId;
        this.reviewBuyerId = reviewBuyerId;
        this.buyerId = buyerId;
        this.reviewBuyerIndicator = reviewBuyerIndicator;
    }
}
