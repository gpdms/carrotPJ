package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewBuyerCustomRepositoryImpl implements ReviewBuyerCustomRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public Long countGoodMessagesByBuyerId(String memId) {
        return queryFactory
                .select(reviewBuyer.message.count())
                .from(reviewBuyer)
                .where(buyerIdEq(memId),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD),
                        reviewBuyer.hideState.eq(HideState.SHOW))
                .fetchOne();
    }

    @Override
    public List<ReviewMessageDto> findGoodMessageListByBuyerIdAndLimitAndCursorId(String buyerId, int limitSize, Long cursorId) {
        List<ReviewBuyerMessageDto> buyerMsgs = queryFactory
                .select(new QReviewBuyerMessageDto(
                        reviewBuyer.reviewBuyerId,
                        reviewBuyer.seller,
                        reviewBuyer.message,
                        reviewBuyer.createdTime))
                .from(reviewBuyer)
                .where(buyerIdEq(buyerId),
                        ltReviewBuyerId(cursorId),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD),
                        reviewBuyer.hideState.eq(HideState.SHOW))
                .orderBy(reviewBuyer.reviewBuyerId.desc())
                .limit(limitSize)
                .fetch();
        return buyerMsgs.stream()
                .map(e -> (ReviewMessageDto) e)
                .collect(Collectors.toList());
    }

    private BooleanExpression ltReviewBuyerId(Long cursorId) {
        if(cursorId == null) {
            return null;
        }
        return reviewBuyer.reviewBuyerId.lt(cursorId);
    }

    @Override
    public List<ReviewMessageDto> findGoodMessageListByBuyerIdAndLimitAndCursorTime(String buyerId, int limitSize, Timestamp cursorTime) {
        List<ReviewBuyerMessageDto> buyerMsgs = queryFactory
                .select(new QReviewBuyerMessageDto(
                        reviewBuyer.reviewBuyerId,
                        reviewBuyer.seller,
                        reviewBuyer.message,
                        reviewBuyer.createdTime))
                .from(reviewBuyer)
                .where(buyerIdEq(buyerId),
                        ltCreatedTime(cursorTime),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD),
                        reviewBuyer.hideState.eq(HideState.SHOW))
                .orderBy(reviewBuyer.reviewBuyerId.desc())
                .limit(limitSize)
                .fetch();
        return buyerMsgs.stream()
                .map(e -> (ReviewMessageDto) e)
                .collect(Collectors.toList());
    }

    private BooleanExpression ltCreatedTime(Timestamp cursorTime) {
        if(cursorTime == null) {
            return null;
        }
        return reviewBuyer.createdTime.lt(cursorTime);
    }


    public BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyer.buyer.memId.eq(memId) : null;
    }
}
