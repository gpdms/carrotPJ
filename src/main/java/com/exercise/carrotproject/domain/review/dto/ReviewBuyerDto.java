package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@NoArgsConstructor
@Getter
@Builder
@ToString
public class ReviewBuyerDto {
    private Long reviewBuyerId;

    @NotNull
    //private Post post_id;
    private String postId;

    @NotNull
    //private Member seller;
    private String sellerId;

    @NotNull
    //private Member buyer;
    private Member buyerId;

    //@NotNull
    private Double totalScore;
    private String message;

    @NotNull
   // @Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    @QueryProjection
    public ReviewBuyerDto(Long reviewBuyerId, String postId, String sellerId, Member buyerId, Double totalScore, String message, ReviewState reviewState, Timestamp createdTime, Timestamp updatedTime) {
        this.reviewBuyerId = reviewBuyerId;
        this.postId = postId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.totalScore = totalScore;
        this.message = message;
        this.reviewState = reviewState;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }
    //Detail 테이블
    //private List<ReviewBuyerDetail> reviewBuyerDetailList;

}
