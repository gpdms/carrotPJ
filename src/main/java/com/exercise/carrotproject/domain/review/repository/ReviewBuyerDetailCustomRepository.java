package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.review.dto.BuyerDetailCountDto;
import com.exercise.carrotproject.domain.review.dto.QBuyerDetailCountDto;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.*;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewBuyerDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
/*    public List<Object> countCommonIndicator(String memId) {
        SELECT t1.review_buyer_indicator, t1.cnt1, t2.cnt2, (t1.cnt1 + t2.cnt2) as sum
        FROM (
                SELECT review_buyer_indicator, count(*) as cnt1
        FROM REVIEW_BUYER_DETAIL
        WHERE buyer_id = 'tester2'
        GROUP BY review_buyer_indicator
) t1
        JOIN (
                SELECT review_seller_indicator, count(*) as cnt2
        FROM REVIEW_SELLER_DETAIL
        WHERE seller_id = 'tester2'
        GROUP BY review_seller_indicator
) t2 ON t1.review_buyer_indicator = t2.review_seller_indicator;
        return  queryFactory.select(new QCommonDetailCountDto())
    }*/

    public List<BuyerDetailCountDto> countIndicatorByBuyer(String memId) {
        return queryFactory.select(
                new QBuyerDetailCountDto(reviewBuyerDetail.reviewBuyerIndicator,
                        reviewBuyerDetail.reviewBuyerIndicator.count()))
                .from(reviewBuyerDetail)
                .where(buyerIdEq(memId))
                .groupBy(reviewBuyerDetail.reviewBuyerIndicator)
                .fetch();
    }

    private BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyerDetail.buyer.memId.eq(memId) : null;
    }
/*    private BooleanExpression buyerIndicatorEq(ReviewBuyerIndicator reviewBuyerIndicator) {
        return reviewBuyerIndicator == null ? null : reviewBuyerDetail.reviewBuyerIndicator.eq(reviewBuyerIndicator);
    }*/


}
