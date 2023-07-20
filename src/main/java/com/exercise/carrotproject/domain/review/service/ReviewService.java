package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId, long limitSize);
    Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId);
    Map<ReviewIndicator, Long> getNegativeReviewIndicators(String memId);

    Long countGoodReviewMessages(String memId);
    Map<String, List<ReviewMessageDto>> collectGoodReviewMessages(String memId);
    List<ReviewMessageDto> getGoodReviewMessageListByLimit(String memId, long limitSize);
}
