package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
public class ReviewSellerDetailDto {
    private Long reviewSellerReviewId;

    @NotNull
    //private ReviewSeller reviewSeller;
    private String reviewSeller_id;

    @NotNull
    //private Member seller;
    private String seller_id;

    @NotNull
    //@Enumerated(value = EnumType.STRING)
    private ReviewSellerIndicator reviewSellerIndicator;

    //private Timestamp createdTime;
}
