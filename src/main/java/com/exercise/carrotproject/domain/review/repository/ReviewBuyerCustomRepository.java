package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBuyerCustomRepository {
    Long countMessageByBuyer(String memId);
    List<ReviewMessageDto> reviewMessageByBuyer(String memId);
    long hideReviewBuyerById(Long reviewBuyerId);
    List<ReviewBuyerDto> getReviewIdsByPostIds(List<Long> postIds);
}
