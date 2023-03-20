package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.DateEntity;
import com.exercise.carrotproject.domain.member.entity.Member;
<<<<<<< Updated upstream:src/main/java/com/exercise/carrotproject/domain/review/entity/ReviewSeller.java
import com.exercise.carrotproject.domain.review.category.converter.ReviewStateConverter;
import com.exercise.carrotproject.post.Post;
import com.exercise.carrotproject.domain.review.category.ReviewState;
=======
import com.exercise.carrotproject.domain.post.Post;
import com.exercise.carrotproject.review.category.ReviewState;
import com.exercise.carrotproject.review.category.converter.ReviewStateConverter;
>>>>>>> Stashed changes:src/main/java/com/exercise/carrotproject/review/entity/ReviewSeller.java
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@DynamicInsert
public class ReviewSeller extends DateEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewSellerId;
    @NotNull @ManyToOne @JoinColumn(name="post_id")
    private Post post;
    @NotNull @ManyToOne @JoinColumn(name="buyer_id") //Member의 pk가 ReviewSeller의 buyer_id(fk)로
    private Member buyer;
    @NotNull @ManyToOne @JoinColumn(name="seller_id") //Member의 pk가 ReviewSeller의 seller_id(fk)로
    private Member seller;
    @NotNull
    private Double totalScore;
    private String message;
    @NotNull @Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    @OneToMany(mappedBy="reviewSeller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewSellerDetail> reviewSellerDetailList;
}
