package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
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

    @NotNull
    //private Post post;
    private String post_id;

    @NotNull
    //private Member buyer;
    private String buyer_id;

    @NotNull
    //private Member seller;
    private String seller_id;

    //@NotNull
    private Double totalScore;
    private String message;

    @NotNull
    //@Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    private Timestamp createdTime;
    private Timestamp updatedTime;

    //Detail테이블
    //private List<ReviewSellerDetail> reviewSellerDetailList;

}
