package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.QReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyer;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.reviewBuyerDetail;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewBuyerCustomRepository {
    private final JPAQueryFactory queryFactory;

    public Long countMessageByBuyer(String memId) {
        return queryFactory
                .select(reviewBuyer.message.count())
                .from(reviewBuyer)
                .where(buyerIdEq(memId),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD))
                .fetchOne();
    }

    public List<ReviewMessageDto> reviewMessageByBuyer(String memId) {
        return queryFactory
                .select(Projections.constructor(ReviewMessageDto.class,
                        reviewBuyer.reviewBuyerId,
                        reviewBuyer.seller,
                        reviewBuyer.message,
                        reviewBuyer.createdTime))
                .from(reviewBuyer)
                .where(buyerIdEq(memId),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD))
                .fetch();
    }

    private BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyer.buyer.memId.eq(memId) : null;
    }

}
