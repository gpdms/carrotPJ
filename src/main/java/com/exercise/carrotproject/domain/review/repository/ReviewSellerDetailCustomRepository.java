package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;

import com.exercise.carrotproject.domain.review.dto.SellerDetailSearchCond;

import com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewSellerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;

   public Long countBySellerAndIndicator (SellerDetailSearchCond condition) {
       return queryFactory
                .select(reviewSellerDetail.count())
                .from(reviewSellerDetail)
                .where(sellerIdEq(condition.getSellerId()),
                        sellerIndicatorEq(condition.getSellerIndicator())
                ).fetchOne();
    };



    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? null : reviewSellerDetail.seller.memId.eq(sellerId);
    }
    private BooleanExpression sellerIndicatorEq(ReviewSellerIndicator reviewSellerIndicator) {
        return reviewSellerIndicator == null ? null : reviewSellerDetail.reviewSellerIndicator.eq(reviewSellerIndicator);
    }

}
