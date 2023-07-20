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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
@Getter
public class ReviewBuyerDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewBuyerDetailId;
    @ManyToOne
    @JoinColumn(name="review_buyer_id", nullable = false)
    private ReviewBuyer reviewBuyer;
    @ManyToOne
    @JoinColumn(name="buyer_id", nullable = false)
    private Member buyer;
    @Enumerated(value = EnumType.STRING)
    private ReviewBuyerIndicator reviewBuyerIndicator;
    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Timestamp createdTime;
}
