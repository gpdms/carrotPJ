package com.exercise.carrotproject.domain.review.repository.detail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Slf4j
@Repository
@Transactional
public class ReviewDetailCustomRepository {
    @PersistenceContext
    private EntityManager em;

    public List<String[]> getPositiveIndicatorListByMemId(String memId) {
        String sql = "SELECT REVIEW_INDICATOR, SUM(COUNT_INDICATOR) AS TOTAL_COUNT " +
                "FROM ( " +
                "       SELECT REVIEW_SELLER_INDICATOR AS REVIEW_INDICATOR, COUNT(REVIEW_SELLER_INDICATOR) AS COUNT_INDICATOR " +
                "       FROM review_seller_detail " +
                "       WHERE REVIEW_SELLER_INDICATOR LIKE '%P%' AND seller_id= :sellerId " +
                "       GROUP BY REVIEW_SELLER_INDICATOR " +
                "       UNION ALL " +
                "       SELECT REVIEW_BUYER_INDICATOR, COUNT(REVIEW_BUYER_INDICATOR) " +
                "       FROM review_buyer_detail " +
                "       WHERE REVIEW_BUYER_INDICATOR LIKE '%P%' AND buyer_id= :buyerId " +
                "       GROUP BY REVIEW_BUYER_INDICATOR " +
                "     ) AS TOTAL " +
                "GROUP BY REVIEW_INDICATOR "+
                "ORDER BY REVIEW_INDICATOR ASC";

        Query query = em.createNativeQuery(sql);
        query.setParameter("sellerId", memId);
        query.setParameter("buyerId", memId);

        return query.getResultList();
    }

    public  List<String[]> getNegativeIndicatorListByMemId(String memId) {
        String sql = "SELECT REVIEW_INDICATOR, SUM(COUNT_INDICATOR) AS TOTAL_COUNT " +
                "FROM ( " +
                "       SELECT REVIEW_SELLER_INDICATOR AS REVIEW_INDICATOR, COUNT(REVIEW_SELLER_INDICATOR) AS COUNT_INDICATOR " +
                "       FROM review_seller_detail " +
                "       WHERE REVIEW_SELLER_INDICATOR LIKE :indicatorName AND seller_id= :sellerId " +
                "       GROUP BY REVIEW_SELLER_INDICATOR " +
                "       UNION ALL " +
                "       SELECT REVIEW_BUYER_INDICATOR, COUNT(REVIEW_BUYER_INDICATOR) " +
                "       FROM review_buyer_detail " +
                "       WHERE REVIEW_BUYER_INDICATOR LIKE '%N%' AND buyer_id= :buyerId " +
                "       GROUP BY REVIEW_BUYER_INDICATOR " +
                "     ) AS TOTAL " +
                "GROUP BY REVIEW_INDICATOR " +
                "ORDER BY REVIEW_INDICATOR ASC";

        Query query = em.createNativeQuery(sql);
        query.setParameter("sellerId", memId);
        query.setParameter("buyerId", memId);

        return query.getResultList();
    }
}
