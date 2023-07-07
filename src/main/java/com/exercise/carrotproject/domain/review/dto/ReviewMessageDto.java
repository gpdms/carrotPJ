package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;


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

    //seller
    public ReviewMessageDto(Long reviewBuyerId, Member seller, String message, Timestamp createdTime) {
        this.reviewBuyerId = reviewBuyerId;
        this.seller = MemberEntityDtoMapper.toMemberDto(seller);
        this.message = message;
        this.createdTime = createdTime;
    }

    //buyer
    public ReviewMessageDto(Member buyer, Long reviewSellerId, String message, Timestamp createdTime) {
        this.buyer = MemberEntityDtoMapper.toMemberDto(buyer);
        this.reviewSellerId = reviewSellerId;
        this.message = message;
        this.createdTime = createdTime;
    }

    public String getCalculatedTimeForReview() {
        return CALCULATE_TIME(this.createdTime);
    }
}
