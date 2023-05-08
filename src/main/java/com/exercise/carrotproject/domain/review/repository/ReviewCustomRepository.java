package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepository {
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory jpaQueryFactory;

    public List<MemberDto> sumScoreForUpdateMannerScore() {
        //2주~1주전 리뷰
        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusWeeks(2));
        Timestamp to = Timestamp.valueOf(LocalDateTime.now().minusWeeks(1));
//        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusMinutes(10));
//        Timestamp to = Timestamp.valueOf(LocalDateTime.now());
        List<MemberDto> rb = jpaQueryFactory.select(
                        Projections.constructor(MemberDto.class, reviewBuyer.buyer.memId, reviewBuyer.totalScore.sum()))
                .from(reviewBuyer)
                .groupBy(reviewBuyer.buyer.memId)
                .where(reviewBuyer.createdTime.between(from, to))
                .fetch();
        List<MemberDto> rs = jpaQueryFactory.select(
                        Projections.constructor(MemberDto.class,
                                reviewSeller.seller.memId,
                                reviewSeller.totalScore.sum()))
                .from(reviewSeller)
                .groupBy(reviewSeller.seller.memId)
                .where(reviewSeller.createdTime.between(from, to))
                .fetch();
        return Stream.concat(rb.stream(), rs.stream())
                .collect(Collectors.groupingBy(MemberDto::getMemId, Collectors.summingDouble(MemberDto::getMannerScore)))
                .entrySet().stream()
                .map(entry -> new MemberDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

/*    public  List<Object[]> getSumScore() {
        String sql = "SELECT mem_id, SUM(total) AS manner_score " +
                "FROM ( " +
                "       SELECT rs.seller_id as mem_id, COUNT(rs.total_score) as total" +
                "       FROM review_seller as rs" +
                "       WHERE rs.created_time BETWEEN DATE_SUB(NOW(), INTERVAL 1 DAY) AND DATE_SUB(NOW(), INTERVAL 1 MINUTE) " +
                "       GROUP BY rs.seller_id " +
                "       UNION ALL " +
                "       SELECT rb.buyer_id, COUNT(rb.total_score) " +
                "       FROM review_buyer as rb " +
                "       WHERE rb.created_time BETWEEN DATE_SUB(NOW(), INTERVAL 1 DAY) AND DATE_SUB(NOW(), INTERVAL 1 MINUTE) " +
                "       GROUP BY rb.buyer_id " +
                "     ) as t " +
                "GROUP BY mem_id " +
                "ORDER BY mem_id";
        Query query = em.createNativeQuery(sql);
        return query.getResultList();
    }*/

}
