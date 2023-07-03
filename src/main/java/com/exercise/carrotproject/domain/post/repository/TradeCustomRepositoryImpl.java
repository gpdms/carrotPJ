package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.post.dto.BuyPostDto;
import com.exercise.carrotproject.domain.post.dto.QBuyPostDto;
import com.exercise.carrotproject.domain.post.entity.QTrade;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.QReviewSeller;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groovyjarjarantlr4.v4.runtime.atn.SemanticContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.post.entity.QTrade.trade;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;

@Repository
@RequiredArgsConstructor
public class TradeCustomRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public List<BuyPostDto> findBuyListByMemId(String memId) {
        return jpaQueryFactory.select(new QBuyPostDto(
                        trade.TradeId,
                        trade.post,
                        trade.buyer.memId,
                        trade.seller.memId,
                        reviewSeller.reviewSellerId
                ))
                .from(trade)
                .rightJoin(reviewBuyer)
                .on(trade.post.postId.eq(reviewBuyer.post.postId))
                .leftJoin(reviewSeller)
                .on(trade.post.postId.eq(reviewSeller.post.postId))
                .where(reviewBuyer.buyer.memId.eq(memId),
                        trade.hideStateBuyer.eq(HideState.SHOW))
                .orderBy(reviewBuyer.createdTime.desc(), reviewBuyer.reviewBuyerId.desc())
                .fetch();
    }
}
