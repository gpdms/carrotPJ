package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;


import com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.reviewBuyerDetail;
import static com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewSellerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;

/*    public List<SellerDetailCountDto> countIndicatorBySeller(String memId) {
        return queryFactory.select(
                        new QSellerDetailCountDto(reviewSellerDetail.reviewSellerIndicator,
                                reviewSellerDetail.reviewSellerIndicator.count()))
                .from(reviewSellerDetail)
                .where(sellerIdEq(memId))
                .groupBy(reviewSellerDetail.reviewSellerIndicator).fetch();
    }*/

    public Map<ReviewSellerIndicator, Long> countSellerIndicatorBySeller(String memId) {
        return queryFactory
                .select(reviewSellerDetail.reviewSellerIndicator, reviewSellerDetail.count())
                .from(reviewSellerDetail)
                .where(sellerIdEq(memId))
                .groupBy(reviewSellerDetail.reviewSellerIndicator)
                .fetchResults()
                .getResults()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(reviewSellerDetail.reviewSellerIndicator),
                        tuple -> tuple.get(reviewSellerDetail.count())
                ));
    }

    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? reviewSellerDetail.seller.memId.eq(sellerId) : null;
    }
/*    private BooleanExpression sellerIndicatorEq(ReviewSellerIndicator reviewSellerIndicator) {
        return reviewSellerIndicator == null ? null : reviewSellerDetail.reviewSellerIndicator.eq(reviewSellerIndicator);
    }*/

}
