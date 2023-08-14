package com.exercise.carrotproject.domain.review.service.message;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageCondition;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewTargetType;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.web.review.response.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewBuyerMessageService implements ReviewMessageService{
    private final ReviewBuyerRepository reviewBuyerRepository;

    @Override
    public boolean supports(ReviewTargetType reviewTargetType) {
        return reviewTargetType == ReviewTargetType.BUYER;
    }

    @Override
    public CursorResult<ReviewMessageDto> getCursorResult(ReviewMessageCondition cond, int limitSize) {
        final List<ReviewMessageDto> messages = getReviewMessageList(cond, limitSize);
        final ReviewMessageDto lastMsg = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        final boolean hasNext = hasNext(lastMsg);
        final Long totalElements = getTotalElements(cond);
        return new CursorResult<>(messages, hasNext, totalElements);
    }

    private List<ReviewMessageDto> getReviewMessageList(ReviewMessageCondition cond, int limitSize) {
        return reviewBuyerRepository
                .findGoodMessageListByBuyerIdAndLimitAndCursorId(cond.getReceiverId(), limitSize, cond.getCursorId());
    }

    private boolean hasNext(ReviewMessageDto lastMsg) {
        if (lastMsg == null) {
            return false;
        } else {
            return reviewBuyerRepository.existsByReviewBuyerIdLessThan(lastMsg.getReviewId());
        }
    }

    private Long getTotalElements(ReviewMessageCondition cond) {
       return reviewBuyerRepository.countGoodMessagesByBuyerId(cond.getReceiverId());
    }

}
