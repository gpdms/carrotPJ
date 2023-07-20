package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;


@Getter
@ToString
public class ReviewMessageDto {
    private Long reviewBuyerId;
    private MemberDto seller;

    private Long reviewSellerId;
    private MemberDto buyer;

    private String message;
    private Timestamp createdTime;

    //to buyer Message
    @QueryProjection
    public ReviewMessageDto(Long reviewBuyerId, Member seller, String message, Timestamp createdTime) {
        this.reviewBuyerId = reviewBuyerId;
        this.seller = MemberEntityDtoMapper.toDto(seller);
        this.message = message;
        this.createdTime = createdTime;
    }

    //to seller Message
    @QueryProjection
    public ReviewMessageDto(Member buyer, Long reviewSellerId, String message, Timestamp createdTime) {
        this.reviewSellerId = reviewSellerId;
        this.buyer = MemberEntityDtoMapper.toDto(buyer);
        this.message = message;
        this.createdTime = createdTime;
    }

    public String getCalculatedTimeForReview() {
        return CALCULATE_TIME(this.createdTime);
    }
}
