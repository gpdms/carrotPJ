package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewBuyerCustomRepository {
    Long countGoodMessagesByBuyerId(String memId);
    List<ReviewMessageDto> getGoodMessageListByBuyerId(String buyerId);
}
