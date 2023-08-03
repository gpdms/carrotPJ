package com.exercise.carrotproject.domain.post.repository;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.member.entity.QMember;
import com.exercise.carrotproject.domain.post.dto.BuyPostDto;
import com.exercise.carrotproject.domain.post.dto.QBuyPostDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.exercise.carrotproject.domain.post.entity.QMtPlace.mtPlace;
import static com.exercise.carrotproject.domain.post.entity.QTrade.trade;
import static com.exercise.carrotproject.domain.review.entity.QReviewBuyer.reviewBuyer;
import static com.exercise.carrotproject.domain.review.entity.QReviewSeller.reviewSeller;

@Repository
@RequiredArgsConstructor
public class TradeCustomRepositoryImpl {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<BuyPostDto> getBuyListPageByMemId(String memId, Pageable pageble) {
        List<BuyPostDto> buyList = getBuyListByMemId(memId, pageble);
        Long count = getBuyListCount(memId);
        return new PageImpl<>(buyList, pageble, count);
    }

    private List<BuyPostDto> getBuyListByMemId(String memId, Pageable pageble) {
        return jpaQueryFactory.select(new QBuyPostDto(
                        trade.tradeId,
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
                .leftJoin(trade.post.mtPlace, mtPlace)
                .fetchJoin()
                .orderBy(reviewBuyer.reviewBuyerId.desc())
                .limit(pageble.getPageSize())
                .offset(pageble.getOffset())
                .fetch();
    }

    private Long getBuyListCount(String memId) {
        return jpaQueryFactory
                .select(trade.count())
                .from(trade)
                .rightJoin(reviewBuyer)
                .on(trade.post.postId.eq(reviewBuyer.post.postId),
                        reviewBuyer.buyer.memId.eq(memId))
                .where(trade.hideStateBuyer.eq(HideState.SHOW))
                .fetchOne();
    }
}
