package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.review.dto.AddReviewRequest;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import org.springframework.stereotype.Service;


@Service
public interface ReviewSellerService {
    ReviewSeller findReviewSellerById(Long reviewSellerId);
    boolean existsReviewSellerByPostId(Long postId);

    void insertReviewSeller(AddReviewRequest request);
    void deleteReviewSellerById(Long reviewSellerId);
    void hideReviewSeller(Long reviewBuyerId);
}
