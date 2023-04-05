package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
//@DynamicInsert
public class ReviewSellerDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewSellerReviewId;

    @ManyToOne
    @JoinColumn(name="review_seller_id")
    private ReviewSeller reviewSeller;

    @ManyToOne
    @JoinColumn(name="seller_id")
    private Member seller;

    @Enumerated(value = EnumType.STRING)
    private ReviewSellerIndicator reviewSellerIndicator;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;
}
