package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.review.dto.BuyerDetailSearchCond;
import com.exercise.carrotproject.domain.review.dto.SellerDetailSearchCond;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail.reviewBuyerDetail;
import static com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail.reviewSellerDetail;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class ReviewDetailCustomRepository {
    private final JPAQueryFactory queryFactory;

/*   public Long countBySellerAndIndicator (SellerDetailSearchCond sellerCond, BuyerDetailSearchCond buyerCond) {
       return queryFactory.select(
                       JPAExpressions.select(reviewSellerDetail.count())
                               .from(reviewSellerDetail)
                               .where(sellerIdEq(sellerCond.getSellerId()),
                                       sellerIndicatorEq(sellerCond.getSellerIndicator()))
                               .add(JPAExpressions.select(reviewBuyerDetail.count())
                                       .from(reviewBuyerDetail)
                                       .where(buyerIdEq(buyerCond.getBuyerId()),
                                               buyerIndicatorEq(buyerCond.getBuyerIndicator()))
                               ))
               .fetchOne();


    };*/

    private BooleanExpression sellerIdEq(String sellerId) {
        return hasText(sellerId) ? null : reviewSellerDetail.seller.memId.eq(sellerId);
    }
    private BooleanExpression sellerIndicatorEq(ReviewSellerIndicator reviewSellerIndicator) {
        return reviewSellerIndicator == null ? null : reviewSellerDetail.reviewSellerIndicator.eq(reviewSellerIndicator);
    }
    private BooleanExpression buyerIdEq(String buyerId) {
        return hasText(buyerId) ? null : reviewBuyerDetail.buyer.memId.eq(buyerId);
    }
    private BooleanExpression buyerIndicatorEq(ReviewBuyerIndicator reviewBuyerIndicator) {
        return reviewBuyerIndicator == null ? null : reviewBuyerDetail.reviewBuyerIndicator.eq(reviewBuyerIndicator);
    }

}
