package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.member.dto.MannerUpdateDto;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.dto.QMannerUpdateDto;
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

    public List<MannerUpdateDto> combineReviewScoreForUpdateMannerScore() {
        LocalDateTime everyMonday5am = LocalDateTime.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(5).withMinute(0).withSecond(0).withNano(0);
        Timestamp from2WeeksAgo = Timestamp.valueOf(everyMonday5am.minusWeeks(2));
        Timestamp to1WeeksAgo = Timestamp.valueOf(everyMonday5am.minusWeeks(1));

        List<MannerUpdateDto> buyerReviewScoreList = jpaQueryFactory.select(
                        new QMannerUpdateDto(reviewBuyer.buyer.memId, reviewBuyer.totalScore.sum()))
                .from(reviewBuyer)
                .groupBy(reviewBuyer.buyer.memId)
                .where(reviewBuyer.createdTime.between(from2WeeksAgo, to1WeeksAgo))
                .fetch();
        List<MannerUpdateDto> sellerReviewScoreList = jpaQueryFactory.select(
                        new QMannerUpdateDto(reviewSeller.seller.memId, reviewSeller.totalScore.sum()))
                .from(reviewSeller)
                .groupBy(reviewSeller.seller.memId)
                .where(reviewSeller.createdTime.between(from2WeeksAgo, to1WeeksAgo))
                .fetch();

        List<MannerUpdateDto> allReviewScoreList = Stream
                .concat(buyerReviewScoreList.stream(), sellerReviewScoreList.stream())
                .collect(Collectors.groupingBy(MannerUpdateDto::getMemId,
                        Collectors.summingDouble(MannerUpdateDto::getReviewScore)))
                .entrySet().stream()
                .map(entry -> new MannerUpdateDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return allReviewScoreList;
    }
}
