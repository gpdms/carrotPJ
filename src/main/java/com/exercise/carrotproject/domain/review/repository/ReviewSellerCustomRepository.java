package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewSellerCustomRepository {
    Long countMessageBySeller(String memId);
    List<ReviewMessageDto> reviewMessageBySeller(String memId);
    long hideReviewSellerById(Long reviewSellerId);
}
