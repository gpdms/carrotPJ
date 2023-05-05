package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewSellerService {
    void insertReviewSeller(ReviewSeller reviewSeller, List<ReviewSellerIndicator> indicatorList);
    void insertReviewSellerDetail(ReviewSeller newReviewSeller, List<ReviewSellerIndicator> indicatorList);
    ReviewSeller findOneReviewSeller(Long reviewSellerId);
    List<ReviewSellerIndicator> getReviewSellerIndicatorsByReview(ReviewSeller reviewSeller);
    Long findReviewSellerIdByPost (Post post);
    void deleteReviewSeller(Long reviewSellerId);
    Map<String, String> hideReviewSeller(Long reviewBuyerId);
}
