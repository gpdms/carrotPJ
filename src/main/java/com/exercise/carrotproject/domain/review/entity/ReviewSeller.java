package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.HideStateConverter;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@ToString (exclude = "reviewSellerDetailList")
@DynamicInsert
public class ReviewSeller extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewSellerId;
    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name="buyer_id", nullable = false)
    private Member buyer;
    @ManyToOne
    @JoinColumn(name="seller_id", nullable = false)
    private Member seller;
    @Column(nullable = false)
    private Double totalScore;
    private String message;
    @Column(nullable = false)
    @Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;
    @ColumnDefault("0")
    @Column(nullable = false)
    @Convert(converter = HideStateConverter.class)
    private HideState hideState;

    @OneToMany(mappedBy="reviewSeller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewSellerDetail> reviewSellerDetailList = new ArrayList<>();

    @Builder
    public ReviewSeller(Long reviewSellerId, Post post, Member buyer, Member seller, Double totalScore, String message, ReviewState reviewState, HideState hideState) {
        this.reviewSellerId = reviewSellerId;
        this.post = post;
        this.buyer = buyer;
        this.seller = seller;
        this.totalScore = totalScore;
        this.message = message;
        this.reviewState = reviewState;
        this.hideState = hideState;
    }

    public Long getReviewSellerId() {
        return reviewSellerId;
    }
    public Post getPost() {
        return post;
    }
    public Member getBuyer() {
        return buyer;
    }
    public Member getSeller() {
        return seller;
    }
    public Double getTotalScore() {
        return totalScore;
    }
    public String getMessage() {
        return message;
    }
    public ReviewState getReviewState() {
        return reviewState;
    }
    public HideState getHideState() {
        return hideState;
    }
    public List<ReviewSellerDetail> getReviewSellerDetailList() {
        return Collections.unmodifiableList(reviewSellerDetailList);
    }

    public List<ReviewSellerIndicator> getReviewSellerIndicatorList() {
        return reviewSellerDetailList.stream()
                .map(ReviewSellerDetail::getReviewSellerIndicator)
                .collect(Collectors.toList());
    }

    public void updateHideState(HideState hideState){
        this.hideState = hideState;
    }
}
