package com.exercise.carrotproject.domain.review.entity;

import com.exercise.carrotproject.domain.common.entity.BaseEntity;
import com.exercise.carrotproject.domain.converter.HideStateConverter;
import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.converter.ReviewStateConverter;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString (exclude = "reviewBuyerDetailList")
@DynamicInsert
public class ReviewBuyer extends BaseEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long reviewBuyerId;
    @ManyToOne
    @JoinColumn(name="post_id", nullable = false)
    private Post post;
    @ManyToOne
    @JoinColumn(name="seller_id", nullable = false)
    private Member seller;
    @ManyToOne
    @JoinColumn(name="buyer_id", nullable = false)
    private Member buyer;
    @Column(nullable = false)
    private Double totalScore;
    private String message;
    @ColumnDefault("0")
    @Column(nullable = false)
    @Convert(converter = HideStateConverter.class)
    private HideState hideState;
    @Column(nullable = false)
    @Convert(converter = ReviewStateConverter.class)
    private ReviewState reviewState;

    @OneToMany(mappedBy="reviewBuyer", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ReviewBuyerDetail> reviewBuyerDetailList = new ArrayList<>();

    @Builder
    private ReviewBuyer(Long reviewBuyerId, Post post, Member seller, Member buyer, Double totalScore, String message, HideState hideState, ReviewState reviewState) {
        this.reviewBuyerId = reviewBuyerId;
        this.post = post;
        this.seller = seller;
        this.buyer = buyer;
        this.totalScore = totalScore;
        this.message = message;
        this.hideState = hideState;
        this.reviewState = reviewState;
    }

    public Long getReviewBuyerId() {
        return reviewBuyerId;
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
    public HideState getHideState() {
        return hideState;
    }
    public ReviewState getReviewState() {
        return reviewState;
    }
    public List<ReviewBuyerDetail> getReviewBuyerDetailList() {
        return Collections.unmodifiableList(reviewBuyerDetailList);
    }

    public List<ReviewBuyerIndicator> getReviewBuyerIndicatorList() {
        return reviewBuyerDetailList.stream()
                .map(ReviewBuyerDetail::getReviewBuyerIndicator)
                .collect(Collectors.toList());
    }

    public void updateHideState(HideState hideState) {
        this.hideState = hideState;
    }
}
