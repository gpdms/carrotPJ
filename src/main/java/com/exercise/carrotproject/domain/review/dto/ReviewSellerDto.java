package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@NoArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class ReviewSellerDto {
    private Long reviewSellerId;

    //private Post post;
    private String postId;

    //private Member buyer;
    private String buyerId;


    //private Member seller;
    private String sellerId;

    //@NotNull
    private Double totalScore;
    private String message;

    //@Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    //Detail테이블
    //private List<ReviewSellerDetail> reviewSellerDetailList;

    @QueryProjection
    public ReviewSellerDto(Long reviewSellerId, String postId, String buyerId, String sellerId, Double totalScore, String message, ReviewState reviewState, Timestamp createdTime, Timestamp updatedTime) {
        this.reviewSellerId = reviewSellerId;
        this.postId = postId;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.totalScore = totalScore;
        this.message = message;
        this.reviewState = reviewState;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
}
