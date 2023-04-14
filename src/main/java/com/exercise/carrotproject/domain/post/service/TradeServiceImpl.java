package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.QTrade;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.TradeRepository;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeServiceImpl {
    private final PostRepository postRepository;
    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final JPAQueryFactory jpaQueryFactory;

    //    @Override
    public Trade selectTradeByPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        Trade trade = tradeRepository.findByPost(post);
        if(trade == null){
            return null;
        }
        return trade;
    }

    //    @Override
    @Transactional
    public void insertTrade(Long postId, String buyerId){
        Post post = postRepository.findById(postId).orElseThrow();
        Member seller = memberRepository.findById(post.getMember().getMemId()).orElseThrow();
        Member buyer = memberRepository.findById(buyerId).orElseThrow();
        Trade trade = Trade.builder()
                .post(post).seller(seller).buyer(buyer).hideStateBuyer(HideState.SHOW).build();
        tradeRepository.save(trade);
    }

    //    @Override
    @Transactional
    public void updateTrade(Long postId, String buyerId){
        Member buyer = memberRepository.findById(buyerId).orElseThrow();

        QTrade qTrade = QTrade.trade;
        jpaQueryFactory
                .update(qTrade)
                .set(qTrade.buyer, buyer)
                .where(qTrade.post.postId.eq(postId))
                .execute();
    }

//    @Override
    @Transactional
    public void deleteTradeAndReview(Long postId){
        Post post = postRepository.findById(postId).orElseThrow();
        //trade테이블에서 delete
            tradeRepository.deleteByPost(post);


    }



}
