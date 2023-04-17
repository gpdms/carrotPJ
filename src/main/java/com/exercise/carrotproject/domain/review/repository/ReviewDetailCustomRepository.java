package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.reviewBuyerDetail;
import static com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail.reviewSellerDetail;
import static org.springframework.util.StringUtils.hasText;

@Repository
@Transactional
@RequiredArgsConstructor
public class ReviewDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final SQLQueryFactory sqlQueryFactory;

    public Map<ReviewIndicator, Long> getMannerDetail(String memId, String keyword) {
        SQLQuery<?> reviewSellerQuery = sqlQueryFactory
                .select(reviewSellerDetail.reviewSellerIndicator.as("reviewIndicator"),
                        reviewSellerDetail.reviewSellerIndicator.count().as("count"))
                .where(reviewSellerDetail.reviewSellerIndicator.stringValue().like("%"+keyword+"%"),
                        sellerIdEq(memId))
                .groupBy(reviewSellerDetail.reviewSellerIndicator);

        SQLQuery<?> reviewBuyerQuery = sqlQueryFactory
                .select(reviewBuyerDetail.reviewBuyerIndicator.as("reviewIndicator"),
                        reviewBuyerDetail.reviewBuyerIndicator.count().as("count"))
                .where(reviewBuyerDetail.reviewBuyerIndicator.stringValue().like("%"+keyword+"%"),
                       buyerIdEq(memId))
                .groupBy(reviewBuyerDetail.reviewBuyerIndicator);

        List<?> fetch = reviewSellerQuery.unionAll(reviewBuyerQuery)
                .fetch();
        List<Tuple> fetch1 = (List<Tuple>) fetch;
        for (Tuple tuple : fetch1) {
            System.out.println("tuple.get(0, String.class)테스트= " + tuple.get(0, String.class));
            System.out.println("tuple.get(1,Long.class)테스트= " + tuple.get(1,Long.class));
        }
        return fetch.stream()
                .map(tuple -> (Tuple) tuple)
                .collect(Collectors.toMap(
                        tuple -> ReviewIndicator.valueOf(tuple.get(0, String.class)),
                        tuple -> tuple.get(1, Long.class),
                        (count1, count2) -> count1 + count2
                ));

    }

    private BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyerDetail.buyer.memId.eq(memId) : null;
    }
    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? reviewSellerDetail.seller.memId.eq(sellerId) : null;
    }
/*    private BooleanExpression buyerIndicatorEq(ReviewBuyerIndicator reviewBuyerIndicator) {
        return reviewBuyerIndicator == null ? null : reviewBuyerDetail.reviewBuyerIndicator.eq(reviewBuyerIndicator);
    }*/


}
