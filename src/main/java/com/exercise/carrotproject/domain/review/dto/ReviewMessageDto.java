package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.*;

import java.sql.Timestamp;

import static com.exercise.carrotproject.domain.common.util.DateUtil.CALCULATE_TIME;


@Getter
@ToString
public class ReviewMessageDto {
    private Long reviewId;
    //"seller", "buyer" 둘 중 하나
    private String senderType;
    private MemberDto sender;
    private String message;
    private Timestamp createdTime;

    ReviewMessageDto(Long reviewId, String senderType, Member sender, String message, Timestamp createdTime) {
        this.reviewId = reviewId;
        this.senderType = senderType;
        this.sender = MemberEntityDtoMapper.toDto(sender);
        this.message = message;
        this.createdTime = createdTime;
    }

    public String getCalculatedTimeForView() {
        return CALCULATE_TIME(this.createdTime);
    }
}

