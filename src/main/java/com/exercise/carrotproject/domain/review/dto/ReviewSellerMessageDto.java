package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.sql.dml.Mapper;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;


@Getter
@ToString
public class ReviewSellerMessageDto extends ReviewMessageDto{

    @QueryProjection
    public ReviewSellerMessageDto(Long reviewSellerId, Member sender, String message, Timestamp createdTime) {
        super(reviewSellerId, "buyer", sender, message, createdTime);
    }
}
