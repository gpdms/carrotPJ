package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.common.entity.Date;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.category.ReviewState;
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
public class ReviewBuyer extends Date {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewBuyerId;

    @NotNull
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @NotNull
    @ManyToOne
    @JoinColumn(name="seller_id") //Member의 pk가 ReviewBuyer의 seller_id(fk)로
    private Member seller;

    @NotNull
    @ManyToOne
    @JoinColumn(name="buyer_id") //Member의 pk가 ReviewBuyer의 buyer_id(fk)로
    private Member buyer;

    @NotNull
    private Double totalScore;

    private String message;

    @NotNull
    @Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    //Detail 테이블
    @OneToMany(mappedBy="reviewBuyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewBuyerDetail> reviewBuyerDetailList;

}
