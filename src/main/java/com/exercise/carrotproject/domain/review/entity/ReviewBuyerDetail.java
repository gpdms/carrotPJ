package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Getter
//@DynamicInsert
public class ReviewBuyerDetail {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewBuyerReviewId;

    @NotNull @ManyToOne @JoinColumn(name="review_buyer_id")
    private ReviewBuyer reviewBuyer;
    @NotNull @ManyToOne @JoinColumn(name="buyer_id")
    private Member buyer;

    @Enumerated(value = EnumType.STRING)
    private ReviewBuyerIndicator reviewBuyerIndicator;

    @CreationTimestamp @Column(updatable = false)
    private Timestamp createdTime;
}
