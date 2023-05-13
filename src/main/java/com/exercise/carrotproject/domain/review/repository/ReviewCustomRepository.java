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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<MemberDto> sumScoreForUpdateMannerScore() {
        // 현재 날짜에서 이번 주 월요일 새벽 5시
        LocalDateTime monday5am = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(5).withMinute(0).withSecond(0).withNano(0);
        //2주~1주전 리뷰
        Timestamp from = Timestamp.valueOf(monday5am.minusWeeks(2));
        Timestamp to = Timestamp.valueOf(monday5am.minusWeeks(1));
        //테스트
//        Timestamp from = Timestamp.valueOf(LocalDateTime.now().minusMinutes(3));
//        Timestamp to = Timestamp.valueOf(LocalDateTime.now());

        List<MemberDto> rb = jpaQueryFactory.select(
                        Projections.constructor(MemberDto.class, reviewBuyer.buyer.memId, reviewBuyer.totalScore.sum()))
                .from(reviewBuyer)
                .groupBy(reviewBuyer.buyer.memId)
                .where(reviewBuyer.createdTime.between(from, to))
                .fetch();
        List<MemberDto> rs = jpaQueryFactory.select(
                        Projections.constructor(MemberDto.class, reviewSeller.seller.memId, reviewSeller.totalScore.sum()))
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


}
