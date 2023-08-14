package com.exercise.carrotproject.domain.review.service.message;

import com.exercise.carrotproject.domain.review.dto.ReviewMessageCondition;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewTargetType;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerRepository;
import com.exercise.carrotproject.web.review.response.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ReviewAllMessageService implements ReviewMessageService{
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewSellerRepository reviewSellerRepository;

    @Override
    public boolean supports(ReviewTargetType reviewTargetType) {
        return reviewTargetType == ReviewTargetType.ALL;
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
        List<ReviewMessageDto> buyerMsgs = reviewBuyerRepository
                .findGoodMessageListByBuyerIdAndLimitAndCursorTime(cond.getReceiverId(), limitSize, cond.getCursorTime());
        List<ReviewMessageDto> sellerMsgs = reviewSellerRepository
                .findGoodMessageListBySellerIdAndLimitAndCursorTime(cond.getReceiverId(), limitSize, cond.getCursorTime());
        return this.concatReviewMessageListByLimit(buyerMsgs, sellerMsgs, limitSize);
    }

    private boolean hasNext(ReviewMessageDto lastMsg) {
        if (lastMsg == null) {
            return false;
        } else  {
            return reviewBuyerRepository.existsByCreatedTimeBefore(lastMsg.getCreatedTime()) ||
                    reviewSellerRepository.existsByCreatedTimeBefore(lastMsg.getCreatedTime());
        }
    }

    private Long getTotalElements(ReviewMessageCondition cond) {
        return reviewBuyerRepository.countGoodMessagesByBuyerId(cond.getReceiverId())
                + reviewSellerRepository.countGoodMessagesBySellerId(cond.getReceiverId());
    }

    private List<ReviewMessageDto> concatReviewMessageListByLimit(List<ReviewMessageDto> buyerMessageList,
                                                                  List<ReviewMessageDto> sellerMessageList,
                                                                  int limitSize) {
        return Stream.concat(buyerMessageList.stream(), sellerMessageList.stream())
                .sorted(Comparator.comparing(ReviewMessageDto::getCreatedTime,
                        Comparator.reverseOrder()))
                .limit(limitSize)
                .collect(Collectors.toList());
    }
}
