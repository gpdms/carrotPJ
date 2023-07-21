package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.QReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public List<ReviewMessageDto> getGoodMessageListByBuyerId(String buyerId) {
        return queryFactory
                .select(new QReviewMessageDto(
                                reviewBuyer.reviewBuyerId,
                                reviewBuyer.seller,
                                reviewBuyer.message,
                                reviewBuyer.createdTime))
                .from(reviewBuyer)
                .where(buyerIdEq(buyerId),
                        reviewBuyer.message.ne(""),
                        reviewBuyer.reviewState.ne(ReviewState.BAD),
                        reviewBuyer.hideState.eq(HideState.SHOW))
                .orderBy(reviewBuyer.createdTime.desc(), reviewBuyer.reviewBuyerId.desc())
                .fetch();
    }

    public BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyer.buyer.memId.eq(memId) : null;
    }
}
