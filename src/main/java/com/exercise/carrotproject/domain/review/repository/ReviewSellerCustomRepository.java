package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewSellerCustomRepository {
    Long countGoodMessagesBySellerId(String memId);
    List<ReviewMessageDto> getGoodMessageListBySellerId(String memId);
}
