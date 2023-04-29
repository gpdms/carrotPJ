package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
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
public class ReviewDetailCustomRepository {
    @PersistenceContext
    private EntityManager em;

    public  List<Object[]> getMannerDetails(String memId, String positiveORnegative) {
        String sql = "SELECT REVIEW_INDICATOR, SUM(COUNT_INDICATOR) AS TOTAL_COUNT " +
                "FROM ( " +
                "       SELECT REVIEW_SELLER_INDICATOR AS REVIEW_INDICATOR, COUNT(REVIEW_SELLER_INDICATOR) AS COUNT_INDICATOR " +
                "       FROM review_seller_detail " +
                "       WHERE REVIEW_SELLER_INDICATOR LIKE :indicatorName AND seller_id= :sellerId " +
                "       GROUP BY REVIEW_SELLER_INDICATOR " +
                "       UNION ALL " +
                "       SELECT REVIEW_BUYER_INDICATOR, COUNT(REVIEW_BUYER_INDICATOR) " +
                "       FROM review_buyer_detail " +
                "       WHERE REVIEW_BUYER_INDICATOR LIKE :indicatorName AND buyer_id= :buyerId " +
                "       GROUP BY REVIEW_BUYER_INDICATOR " +
                "     ) AS T " +
                "GROUP BY REVIEW_INDICATOR " +
                "ORDER BY REVIEW_INDICATOR";


        Query query = em.createNativeQuery(sql);
        query.setParameter("indicatorName", "%" + positiveORnegative + "%");
        query.setParameter("sellerId", memId);
        query.setParameter("buyerId", memId);

        return query.getResultList();
    }

}
