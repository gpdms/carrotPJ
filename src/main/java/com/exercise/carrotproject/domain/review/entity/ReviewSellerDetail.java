package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@ToString
public class ReviewSellerDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewSellerDetailId;
    @ManyToOne
    @JoinColumn(name="review_seller_id", nullable = false)
    private ReviewSeller reviewSeller;
    @ManyToOne
    @JoinColumn(name="seller_id", nullable = false)
    private Member seller;
    @Enumerated(value = EnumType.STRING)
    private ReviewSellerIndicator reviewSellerIndicator;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdTime;
}
