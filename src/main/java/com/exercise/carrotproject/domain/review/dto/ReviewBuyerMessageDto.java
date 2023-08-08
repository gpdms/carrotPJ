package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;


@Getter
@ToString
public class ReviewBuyerMessageDto extends ReviewMessageDto {
    @QueryProjection
    public ReviewBuyerMessageDto(Long reviewBuyerId, Member sender, String message, Timestamp createdTime) {
        super(reviewBuyerId, "seller", sender, message, createdTime);
    }
}
