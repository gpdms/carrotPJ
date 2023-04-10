package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ReviewMessageDto {
    private Long reviewBuyerId;
    private Long reviewSellerId;

    private MemberDto seller;
    private MemberDto buyer;

    private String message;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    public ReviewMessageDto(Long reviewBuyerId, Member seller, String message, Timestamp createdTime) {
        this.reviewBuyerId = reviewBuyerId;
        this.seller = MemberEntityDtoMapper.toMemberDto(seller);
        this.message = message;
        this.createdTime = createdTime;
    }

    public ReviewMessageDto(Member buyer, Long reviewSellerId, String message, Timestamp createdTime) {
        this.buyer = MemberEntityDtoMapper.toMemberDto(buyer);
        this.reviewSellerId = reviewSellerId;
        this.message = message;
        this.createdTime = createdTime;
    }
}
