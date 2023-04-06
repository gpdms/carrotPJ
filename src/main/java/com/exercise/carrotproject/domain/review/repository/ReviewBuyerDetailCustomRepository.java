package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.review.dto.BuyerDetailSearchCond;
import com.exercise.carrotproject.domain.review.dto.SellerDetailSearchCond;

import com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewBuyerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
    public Long countByBuyerAndIndicator (BuyerDetailSearchCond condition) {
        return queryFactory
                .select(reviewBuyerDetail.count())
                .from(reviewBuyerDetail)
                .where(buyerIdEq(condition.getBuyerId()),
                        buyerIndicatorEq(condition.getBuyerIndicator())
                ).fetchOne();
    };

    private BooleanExpression buyerIdEq(String buyerId) {
        return hasText(buyerId) ? null : reviewBuyerDetail.buyer.memId.eq(buyerId);
    }
    private BooleanExpression buyerIndicatorEq(ReviewBuyerIndicator reviewBuyerIndicator) {
        return reviewBuyerIndicator == null ? null : reviewBuyerDetail.reviewBuyerIndicator.eq(reviewBuyerIndicator);
    }


}
