package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.SMember;
import com.exercise.carrotproject.domain.review.dto.SReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.dto.SReviewSellerDetail;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.sql.JPASQLQuery;
import com.querydsl.sql.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class ReviewDetailCustomRepository {
    private final JPAQueryFactory queryFactory;
    private final SQLQueryFactory sqlQueryFactory;
    @PersistenceContext
    EntityManager em;
    private final SQLTemplates sqlTemplates;
    //네이티브 쿼리
    public  Map<ReviewIndicator, Long> getMannerDetails(String memId, String positiveORnegative) {
        String sql = "SELECT REVIEW_INDICATOR, SUM(COUNT_INDICATOR) AS TOTAL_COUNT "
                + "FROM ( "
                + "       SELECT REVIEW_SELLER_INDICATOR AS REVIEW_INDICATOR, COUNT(REVIEW_SELLER_INDICATOR) AS COUNT_INDICATOR "
                + "       FROM REVIEW_SELLER_DETAIL "
                + "       WHERE REVIEW_SELLER_INDICATOR LIKE :indicatorName AND seller_id= :sellerId "
                + "       GROUP BY REVIEW_SELLER_INDICATOR "
                + "       UNION ALL "
                + "       SELECT REVIEW_BUYER_INDICATOR, COUNT(REVIEW_BUYER_INDICATOR) "
                + "       FROM REVIEW_BUYER_DETAIL "
                + "       WHERE REVIEW_BUYER_INDICATOR LIKE :indicatorName AND buyer_id= :buyerId "
                + "       GROUP BY REVIEW_BUYER_INDICATOR "
                + "     ) "
                + "GROUP BY REVIEW_INDICATOR "
                + "ORDER BY REVIEW_INDICATOR";

        Query query = em.createNativeQuery(sql);
        query.setParameter("indicatorName", "%" + positiveORnegative + "%");
        query.setParameter("sellerId", memId);
        query.setParameter("buyerId", memId);

        List<Object[]> resultList = query.getResultList();

        return resultList
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf((String) row[0]),
                        row -> ((BigDecimal) row[1]).longValue()
                ));
    }

    //jpaSQLQuery
    @Transactional(readOnly = true)
    public  Map<ReviewIndicator, Long> getMannerDetailsByJPASQLQuery(String memId, String keyword) {
        JPASQLQuery<?> jpaSqlQuery = new JPASQLQuery<>(em, sqlTemplates);

        List<Tuple> tuples = jpaSqlQuery.select(reviewSellerDetail.reviewSellerIndicator.as("review_indicator"),
                        reviewSellerDetail.count().as("count"))
                .from(reviewSellerDetail)
                .where(reviewSellerDetail.reviewSellerIndicator.stringValue().like("%" + keyword + "%"),
                        reviewSellerDetail.seller.memId.eq(memId))
                .groupBy(reviewSellerDetail.reviewSellerIndicator)
                .unionAll(
                        JPAExpressions.select(reviewBuyerDetail.reviewBuyerIndicator.as("review_indicator"),
                                        reviewBuyerDetail.count().as("count"))
                                .from(reviewBuyerDetail)
                                .where(reviewBuyerDetail.reviewBuyerIndicator.stringValue().like("%" + keyword + "%"),
                                        reviewBuyerDetail.buyer.memId.eq(memId))
                                .groupBy(reviewBuyerDetail.reviewBuyerIndicator)
                ).fetch();
        return tuples.stream()
                .collect(Collectors.toMap(
                        tuple -> ReviewIndicator.valueOf(tuple.get(0, String.class)),
                        tuple -> tuple.get(1, Long.class),
                        (count1, count2) -> count1 + count2
                ));
    }

    public Map<ReviewIndicator, Long> getMannerDetailsBySqlQueryFactory(String memId, String keyword) {
//        NumberPath<Integer> countIndicator = Expressions.numberPath(Integer.class, "countIndicator");
//        EnumPath<ReviewIndicator> reviewIndicator = Expressions.enumPath(ReviewIndicator.class, "reviewIndicator");
        SReviewSellerDetail sReviewSellerDetail = new SReviewSellerDetail("review_seller_detail");
        SReviewBuyerDetail sReviewBuyerDetail = new SReviewBuyerDetail("review_buyer_detail");
        SQLQuery<?> reviewSellerQuery = sqlQueryFactory.query()
                .select(sReviewSellerDetail.reviewSellerIndicator.stringValue().as("reviewIndicator"),
                        sReviewSellerDetail.reviewSellerIndicator.count().as("count"))
                .from(sReviewSellerDetail)
                .where(sReviewSellerDetail.reviewSellerIndicator.stringValue().like("%" + keyword + "%"),
                        sellerIdEq(memId))
                .groupBy(sReviewSellerDetail.reviewSellerIndicator);

        SQLQuery<?> reviewBuyerQuery = sqlQueryFactory.query()
                .select(sReviewBuyerDetail.reviewBuyerIndicator.as("reviewIndicator"),
                        sReviewBuyerDetail.reviewBuyerIndicator.count().as("count"))
                .from(sReviewBuyerDetail)
                .where(sReviewBuyerDetail.reviewBuyerIndicator.stringValue().like("%"+keyword+"%"),
                        buyerIdEq(memId))
                .groupBy(sReviewBuyerDetail.reviewBuyerIndicator);

        List<?> fetch = reviewSellerQuery.unionAll(reviewBuyerQuery).fetch();

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
    public List<String> getMannerDetail2() {
        SReviewSellerDetail sReviewSellerDetail = new SReviewSellerDetail("review_seller_detail");
//        List<String> fetch = sqlQueryFactory.select(sReviewSellerDetail.reviewSellerIndicator)
//                .from(sReviewSellerDetail)
//                .fetch().stream()
//                .map(ReviewIndicator::valueOf)
//                .collect(Collectors.toList());
    /*    fetch.stream()
                .map(ReviewIndicator::valueOf)
                .collect(Collectors.toList());*/
//        log.info("뭐지 {}", fetch);
//        for (ReviewIndicator reviewIndicator : fetch) {
//            log.info("리뷰 {}");
//        }
        return sqlQueryFactory.select(sReviewSellerDetail.reviewSellerIndicator.stringValue())
                .from(sReviewSellerDetail)
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
