package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.member.entity.Member;
<<<<<<< Updated upstream:src/main/java/com/exercise/carrotproject/domain/review/entity/ReviewSellerDetail.java
import com.exercise.carrotproject.domain.review.category.ReviewSellerIndicator;
=======
import com.exercise.carrotproject.review.category.ReviewSellerIndicator;
>>>>>>> Stashed changes:src/main/java/com/exercise/carrotproject/review/entity/ReviewSellerDetail.java
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@DynamicInsert
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
