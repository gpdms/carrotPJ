package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.member.entity.SMember;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.sql.SQLExpressions;
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


import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;


import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

@Repository
@Transactional
@RequiredArgsConstructor
public class ReviewDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final SQLQueryFactory sqlQueryFactory;

    public Map<ReviewIndicator, Long> getMannerDetail(String memId, String keyword) {
//        NumberPath<Integer> countIndicator = Expressions.numberPath(Integer.class, "countIndicator");
//        EnumPath<ReviewIndicator> reviewIndicator = Expressions.enumPath(ReviewIndicator.class, "reviewIndicator");

        SQLQuery<?> subQuery1 = SQLExpressions
                .select(reviewSellerDetail.reviewSellerIndicator.as("reviewIndicator"),
                        reviewSellerDetail.reviewSellerIndicator.count().as("count"))
                .from(reviewSellerDetail)
                .where(reviewSellerDetail.reviewSellerIndicator.stringValue().like("%"+keyword+"%"),
                        sellerIdEq(memId))
                .groupBy(reviewSellerDetail.reviewSellerIndicator);

        SQLQuery<?> subQuery2 = SQLExpressions
                .select(reviewBuyerDetail.reviewBuyerIndicator.as("reviewIndicator"),
                        reviewBuyerDetail.reviewBuyerIndicator.count().as("count"))
                .from(reviewBuyerDetail)
                .where(reviewBuyerDetail.reviewBuyerIndicator.stringValue().like("%"+keyword+"%"),
                        buyerIdEq(memId))
                .groupBy(reviewBuyerDetail.reviewBuyerIndicator);

//        List<?> fetch = sqlQueryFactory.query().unionAll(reviewSellerQuery, reviewBuyerQuery).fetch();
        List<?> fetch = sqlQueryFactory
                .select(subQuery1.unionAll(subQuery2))
                .fetch();
        SQLExpressions.unionAll();

/*        for (Tuple tuple : fetch) {
            System.out.println("tuple.get(0, String.class)테스트= " + tuple.get(0, String.class));
            System.out.println("tuple.get(1,Long.class)테스트= " + tuple.get(1,Long.class));
        }*/
        return fetch.stream()
                .map(tuple -> (Tuple) tuple)
                .collect(Collectors.toMap(
                        tuple -> ReviewIndicator.valueOf(tuple.get(0, String.class)),
                        tuple -> tuple.get(1, Long.class),
                        (count1, count2) -> count1 + count2
                ));

    }
    public List<Long> getMannerDetail2() {
        return sqlQueryFactory.select(reviewSellerDetail.reviewSellerDetailId)
                .from(reviewBuyerDetail)
                .fetch();
    }
    public List<String> getMannerDetail3() {
        //QMember sMember = new QMember("member");
        SMember sMember = new SMember("member");
        return sqlQueryFactory.select(sMember.memId)
                .from(sMember)
                .fetch();
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
