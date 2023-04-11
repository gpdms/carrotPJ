package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
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
        this.buyer = MemberEntityDtoMapper.toMemberDto(buyer);
        this.message = message;
        this.createdTime = createdTime;
    }
}
