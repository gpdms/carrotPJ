package com.exercise.carrotproject.domain.post.service;

import com.exercise.carrotproject.domain.post.entity.Trade;
import org.springframework.stereotype.Service;

@Service
public interface TradeService {
    Trade selectTradeByPost(Long postId);
    void insertTrade(Long postId, String buyerId);
    void updateTrade(Long postId, String buyerId);
    void deleteTradeAndReview(Long postId);

}
