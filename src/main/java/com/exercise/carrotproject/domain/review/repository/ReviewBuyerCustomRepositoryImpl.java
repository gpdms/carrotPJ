package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.QReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
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
    public Long countMessageByBuyer(String memId) {
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
                        reviewBuyer.reviewState.ne(ReviewState.BAD),
                        reviewBuyer.hideState.eq(HideState.SHOW))
                .fetch();
    }
    @Override
    public long hideReviewBuyerById(Long reviewBuyerId) {
        return queryFactory.update(reviewBuyer)
                .set(reviewBuyer.hideState, HideState.HIDE)
                .where(reviewBuyer.reviewBuyerId.eq(reviewBuyerId))
                .execute();
    }
    @Override
    public List<ReviewBuyerDto> getReviewIdsByPostIds(List<Long> postIds) {
       return queryFactory.select(new QReviewBuyerDto(reviewBuyer.post.postId, reviewBuyer.reviewBuyerId)
                ).from(reviewBuyer)
                .where(reviewBuyer.post.postId.in(postIds))
                .fetch();
    }

    public BooleanExpression buyerIdEq(String memId) {
        return hasText(memId) ? reviewBuyer.buyer.memId.eq(memId) : null;
    }

}
