package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReviewSellerCustomRepository {
    Long countGoodMessagesBySellerId(String memId);
    List<ReviewMessageDto> findGoodMessageListBySellerIdAndLimitAndCursorId(String sellerId, int limitSize, Long cursorId);
    List<ReviewMessageDto> findGoodMessageListBySellerIdAndLimitAndCursorTime(String sellerId, int limitSize, Timestamp cursorTime);
}
