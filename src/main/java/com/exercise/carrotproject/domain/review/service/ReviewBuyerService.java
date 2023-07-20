package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.review.dto.AddReviewRequest;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import org.springframework.stereotype.Service;

@Service
public interface ReviewBuyerService {
    ReviewBuyer findReviewBuyerById(Long reviewBuyerId);
    boolean existsReviewBuyerByPostId(Long postId);

    void insertReviewBuyer(AddReviewRequest request);
    void deleteReviewBuyerById(Long reviewBuyerId);
    void hideReviewBuyer(Long reviewSellerId);
}
