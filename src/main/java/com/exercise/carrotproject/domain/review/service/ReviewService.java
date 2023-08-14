package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageCondition;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.web.review.response.CursorResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    Map<ReviewIndicator, Long> getPositiveReviewIndicatorsByLimit(String memId, int limitSize);
    Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId);
    Map<ReviewIndicator, Long> getNegativeReviewIndicators(String memId);

    Long countAllGoodReviewMessages(String memId);
    List<ReviewMessageDto> getAllRecentGoodReviewMessageListByLimit(String memId, int limitSize);
    Map<String, CursorResult<ReviewMessageDto>> collectRecentGoodReviewMessagesByLimit(String memId, int limitSize);
    CursorResult<ReviewMessageDto> getReviewMessageCursorResult(ReviewMessageCondition cond, int limitSize);
}
