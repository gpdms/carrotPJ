package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewBuyerService {
    void insertReviewBuyer(ReviewBuyer reviewBuyer, List<ReviewBuyerIndicator> indicatorList);
    void insertReviewBuyerDetail(ReviewBuyer newReviewBuyer, List<ReviewBuyerIndicator> indicatorList);
    ReviewBuyer findOneReviewBuyer(Long reviewBuyerId);
    Long findReviewBuyerIdByPost(Post post);
    List<ReviewBuyerIndicator> getReviewBuyerIndicatorsByReview(ReviewBuyer reviewBuyer);
    void deleteReviewBuyer(Long reviewBuyerId);
    Map<String, String> hideReviewBuyer(Long reviewSellerId);
    List<ReviewBuyerDto> findReviewsByPostId(List<Long> postIds);

}
