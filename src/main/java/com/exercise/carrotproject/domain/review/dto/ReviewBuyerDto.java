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
public class ReviewBuyerDto {
    private Long reviewBuyerId;

    private Long postId;

    private MemberDto seller;

    private MemberDto buyer;

    private Double totalScore;
    private String message;

    @NotNull
    private ReviewState reviewState;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    @QueryProjection
    public ReviewBuyerDto(Long reviewBuyerId, Member seller, String message, Timestamp createdTime) {
        this.reviewBuyerId = reviewBuyerId;
        this.seller = MemberEntityDtoMapper.toMemberDto(seller);
        this.message = message;
        this.createdTime = createdTime;
    }

    @QueryProjection
    public ReviewBuyerDto(Long reviewBuyerId, Long postId) {
     this.reviewBuyerId = reviewBuyerId;
      this.postId = postId;
    }
}
