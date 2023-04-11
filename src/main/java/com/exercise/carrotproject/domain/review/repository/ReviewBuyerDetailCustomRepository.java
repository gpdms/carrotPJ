package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;

import java.util.Comparator;
import java.util.TreeMap;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewBuyerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
/*    public List<BuyerDetailCountDto> countIndicatorByBuyer(String memId) {
        return queryFactory.select(
                new QBuyerDetailCountDto(reviewBuyerDetail.reviewBuyerIndicator,
                        reviewBuyerDetail.reviewBuyerIndicator.count()))
                .from(reviewBuyerDetail)
                .where(buyerIdEq(memId))
                .groupBy(reviewBuyerDetail.reviewBuyerIndicator)
                .fetch();
    }*/

    public Map<ReviewBuyerIndicator, Long> countBuyerIndicatorByBuyer(String memId) {
        return queryFactory.select(reviewBuyerDetail.reviewBuyerIndicator, reviewBuyerDetail.count())
                .from(reviewBuyerDetail)
                .where(buyerIdEq(memId))
                .groupBy(reviewBuyerDetail.reviewBuyerIndicator)
                .fetchResults()
                .getResults()
                .stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(reviewBuyerDetail.reviewBuyerIndicator),
                        tuple -> tuple.get(reviewBuyerDetail.count())
                ));
    }

    private BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyerDetail.buyer.memId.eq(memId) : null;
    }
/*    private BooleanExpression buyerIndicatorEq(ReviewBuyerIndicator reviewBuyerIndicator) {
        return reviewBuyerIndicator == null ? null : reviewBuyerDetail.reviewBuyerIndicator.eq(reviewBuyerIndicator);
    }*/


}
