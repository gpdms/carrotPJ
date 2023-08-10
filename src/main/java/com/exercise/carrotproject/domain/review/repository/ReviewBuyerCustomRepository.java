package com.exercise.carrotproject.domain.review.repository;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReviewBuyerCustomRepository {
    Long countGoodMessagesByBuyerId(String memId);
    List<ReviewMessageDto> findGoodMessageListByBuyerIdAndLimitAndCursorId(String buyerId, int limitSize, Long cursorId);
    List<ReviewMessageDto> findGoodMessageListByBuyerIdAndLimitAndCursorTime(String buyerId, int limitSize, Timestamp cursorTime);
}
