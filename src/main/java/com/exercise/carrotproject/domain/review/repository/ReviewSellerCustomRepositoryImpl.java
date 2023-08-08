package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.review.dto.*;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ReviewMessageDto> findGoodMessageListBySellerIdAndLimitAndCursorId(String sellerId, int limitSize, Long cursorId) {
        List<ReviewSellerMessageDto> sellerMsgs = queryFactory
                .select(new QReviewSellerMessageDto(
                        reviewSeller.reviewSellerId,
                        reviewSeller.buyer,
                        reviewSeller.message,
                        reviewSeller.createdTime))
                .from(reviewSeller)
                .where(sellerIdEq(sellerId),
                        ltReviewSellerId(cursorId),
                        reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .orderBy(reviewSeller.reviewSellerId.desc())
                .limit(limitSize)
                .fetch();
        return sellerMsgs.stream()
                .map(e -> (ReviewMessageDto) e)
                .collect(Collectors.toList());
    }

    private BooleanExpression ltReviewSellerId(Long cursorId) {
        if(cursorId == null) {
            return null;
        }
        return reviewSeller.reviewSellerId.lt(cursorId);
    }

    @Override
    public List<ReviewMessageDto> findGoodMessageListBySellerIdAndLimitAndCursorTime(String sellerId, int limitSize, Timestamp cursorTime) {
        List<ReviewSellerMessageDto> sellerMsgs = queryFactory
                .select(new QReviewSellerMessageDto(
                        reviewSeller.reviewSellerId,
                        reviewSeller.buyer,
                        reviewSeller.message,
                        reviewSeller.createdTime))
                .from(reviewSeller)
                .where(sellerIdEq(sellerId),
                        ltCreatedTime(cursorTime),
                        reviewSeller.message.ne(""),
                        reviewSeller.reviewState.ne(ReviewState.BAD),
                        reviewSeller.hideState.eq(HideState.SHOW))
                .orderBy(reviewSeller.reviewSellerId.desc())
                .limit(limitSize)
                .fetch();
        return sellerMsgs.stream()
                .map(e -> (ReviewMessageDto) e)
                .collect(Collectors.toList());
    }

    private BooleanExpression ltCreatedTime(Timestamp cursorTime) {
        if(cursorTime == null) {
            return null;
        }
        return reviewSeller.createdTime.lt(cursorTime);
    }

    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? reviewSeller.seller.memId.eq(sellerId) : null;
    }

}
