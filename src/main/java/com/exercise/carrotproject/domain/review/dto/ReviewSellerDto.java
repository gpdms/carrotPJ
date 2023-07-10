package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.util.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class ReviewSellerDto {
    private Long reviewSellerId;

    private String postId;

    private MemberDto seller;

    private MemberDto buyer;

    private Double totalScore;
    private String message;

    private ReviewState reviewState;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    @QueryProjection
    public ReviewSellerDto(Long reviewSellerId, Member buyer, String message, Timestamp createdTime) {
        this.reviewSellerId = reviewSellerId;
        this.buyer = MemberEntityDtoMapper.toDto(buyer);
        this.message = message;
        this.createdTime = createdTime;
    }
}
