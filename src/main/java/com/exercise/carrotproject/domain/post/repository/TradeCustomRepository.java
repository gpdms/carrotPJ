package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.post.entity.QTrade;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyer;
import com.querydsl.jpa.impl.JPAQueryFactory;
import groovyjarjarantlr4.v4.runtime.atn.SemanticContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.exercise.carrotproject.domain.post.entity.QTrade.trade;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;

@Repository
@RequiredArgsConstructor
public class TradeCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public List<Trade> getBuyList(String memId) {
        return jpaQueryFactory.select(trade).from(trade)
                .rightJoin(reviewBuyer)
                .on(trade.post.postId.eq(reviewBuyer.post.postId))
                .where(reviewBuyer.buyer.memId.eq(memId),
                        trade.hideStateBuyer.eq(HideState.SHOW))
                .distinct()
                .fetch();
    }
}
