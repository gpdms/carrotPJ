package com.exercise.carrotproject.domain.review.service.message;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageCondition;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewTargetType;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerRepository;
import com.exercise.carrotproject.web.review.response.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewSellerMessageService implements ReviewMessageService{
    private final ReviewSellerRepository reviewSellerRepository;

    @Override
    public boolean supports(ReviewTargetType reviewTargetType) {
        return reviewTargetType == ReviewTargetType.SELLER;
    }

    @Override
    public CursorResult<ReviewMessageDto> getCursorResult(ReviewMessageCondition cond, int limitSize) {
        final List<ReviewMessageDto> messages = getReviewMessageList(cond, limitSize);
        final ReviewMessageDto lastMsg = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        final boolean hasNext = hasNext(lastMsg);
        final Long totalElements = getTotalElements(cond);
        return new CursorResult<>(messages, hasNext, totalElements);
    }

    private List<ReviewMessageDto> getReviewMessageList(ReviewMessageCondition cond,
                                                        int limitSize) {
        return reviewSellerRepository
                .findGoodMessageListBySellerIdAndLimitAndCursorId(cond.getReceiverId(), limitSize, cond.getCursorId());
    }

    private boolean hasNext(ReviewMessageDto lastMsg) {
        if (lastMsg == null) {
            return false;
        } else {
            return reviewSellerRepository.existsByReviewSellerIdLessThan(lastMsg.getReviewId());
        }
    }

    private Long getTotalElements(ReviewMessageCondition cond) {
            return reviewSellerRepository.countGoodMessagesBySellerId(cond.getReceiverId());
    }
}
