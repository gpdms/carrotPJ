package com.exercise.carrotproject.domain.review.service;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ReviewService {
    Map<ReviewIndicator, Long> getPositiveMannerDetailsBrief(String memId, long limitSize);
    Map<ReviewIndicator, Long> getPositiveMannerDetails (String memId);
    Map<ReviewIndicator, Long> getNegativeMannerDetails (String memId);
    Long countGoodReviewMessage(String memId);
    Map<String, List<ReviewMessageDto>> goodReviewMessagesDetail(String memId);
    List<ReviewMessageDto> goodReviewMessagesBrief(String memId, long limitSize);
}
