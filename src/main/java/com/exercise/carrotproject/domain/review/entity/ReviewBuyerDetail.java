package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
<<<<<<< Updated upstream:src/main/java/com/exercise/carrotproject/domain/review/entity/ReviewBuyerDetail.java
import com.exercise.carrotproject.domain.review.category.ReviewBuyerIndicator;
=======
import com.exercise.carrotproject.review.category.ReviewBuyerIndicator;
>>>>>>> Stashed changes:src/main/java/com/exercise/carrotproject/review/entity/ReviewBuyerDetail.java
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class ReviewBuyerDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewBuyerReviewId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="review_buyer_id")
    private ReviewBuyer reviewBuyer;

    @NotNull
    @ManyToOne
    @JoinColumn(name="buyer_id")
    private Member member;

    @Enumerated(value = EnumType.STRING)
    private ReviewBuyerIndicator reviewBuyerIndicator;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdTime;
}
