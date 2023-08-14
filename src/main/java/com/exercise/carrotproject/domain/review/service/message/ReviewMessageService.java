package com.exercise.carrotproject.domain.review.service.message;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageCondition;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewTargetType;
import com.exercise.carrotproject.web.review.response.CursorResult;

import java.util.List;

public interface ReviewMessageService {
    boolean supports(ReviewTargetType reviewTargetType);
    CursorResult<ReviewMessageDto> getCursorResult(ReviewMessageCondition cond, int limitSize);
}
