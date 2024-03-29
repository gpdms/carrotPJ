package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
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
public class ReviewSellerDetailDto {
    private Long reviewSellerReviewId;
    private String reviewSellerId;
    private String sellerId;
    private ReviewSellerIndicator reviewSellerIndicator;

    @QueryProjection
    public ReviewSellerDetailDto(Long reviewSellerReviewId, String reviewSellerId, String sellerId, ReviewSellerIndicator reviewSellerIndicator) {
        this.reviewSellerReviewId = reviewSellerReviewId;
        this.reviewSellerId = reviewSellerId;
        this.sellerId = sellerId;
        this.reviewSellerIndicator = reviewSellerIndicator;
    }
}
