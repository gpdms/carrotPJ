package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.*;
import com.exercise.carrotproject.domain.review.entity.QReviewSeller;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;
import static com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail.reviewSellerDetail;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewSellerCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Long countMessageBySeller(String memId) {
        return queryFactory
                .select(reviewSeller.message.count())
                .from(reviewSeller)
                .where(sellerIdEq(memId), reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .fetchOne();
    }

    public List<ReviewMessageDto> reviewMessageBySeller(String memId) {
        return queryFactory
                .select(Projections.constructor(ReviewMessageDto.class,
                        reviewSeller.buyer,
                        reviewSeller.reviewSellerId,
                        reviewSeller.message,
                        reviewSeller.createdTime))
                .from(reviewSeller)
                .where(sellerIdEq(memId),
                        reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .fetch();
    }

    public long hideReviewSellerById(Long reviewSellerId) {
        return queryFactory.update(reviewSeller)
                .set(reviewSeller.hideState, HideState.HIDE)
                .where(reviewSeller.reviewSellerId.eq(reviewSellerId))
                .execute();
    }

    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? reviewSeller.seller.memId.eq(sellerId) : null;
    }

}
