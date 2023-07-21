package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewSellerCustomRepositoryImpl implements ReviewSellerCustomRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public Long countGoodMessagesBySellerId(String memId) {
        return queryFactory
                .select(reviewSeller.message.count())
                .from(reviewSeller)
                .where(sellerIdEq(memId), reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .fetchOne();
    }
    @Override
    public List<ReviewMessageDto> getGoodMessageListBySellerId(String memId) {
        return queryFactory
                .select(Projections.constructor(ReviewMessageDto.class,
                        reviewSeller.buyer,
                        reviewSeller.reviewSellerId,
                        reviewSeller.message,
                        reviewSeller.createdTime))
                .from(reviewSeller)
                .where(sellerIdEq(memId),
                        reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .fetch();
    }

    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? reviewSeller.seller.memId.eq(sellerId) : null;
    }

}
