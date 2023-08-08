package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.*;
import com.exercise.carrotproject.domain.review.repository.*;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewDetailCustomRepository;
import com.exercise.carrotproject.web.review.response.CursorResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService{
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewDetailCustomRepository reviewDetailCustomRepository;

    @Override
    public Map<ReviewIndicator, Long> getPositiveReviewIndicatorsByLimit(String memId, int limitSize) {
        return reviewDetailCustomRepository.getPositiveIndicatorListByMemId(memId)
                .stream()
                .limit(limitSize)
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(String.valueOf(row[0])),
                        row -> Long.valueOf(String.valueOf(row[1])),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId) {
      return reviewDetailCustomRepository.getPositiveIndicatorListByMemId(memId)
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(String.valueOf(row[0])),
                        row -> Long.valueOf(String.valueOf(row[1])),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<ReviewIndicator, Long> getNegativeReviewIndicators(String memId) {
       return reviewDetailCustomRepository.getNegativeIndicatorListByMemId(memId)
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(String.valueOf(row[0])),
                        row -> Long.valueOf(String.valueOf(row[1])),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Long countAllGoodReviewMessages(String memId) {
        return reviewBuyerRepository.countGoodMessagesByBuyerId(memId)
                + reviewSellerRepository.countGoodMessagesBySellerId(memId) ;
    }

    @Override
    public Map<String, CursorResult<ReviewMessageDto>> collectRecentGoodReviewMessagesByLimit(String memId, int limitSize) {
        ReviewMessageCondition buyerCond = ReviewMessageCondition.builder()
                .receiverId(memId)
                .reviewTargetType(ReviewTargetType.BUYER)
                .cursorId(null).build();
        CursorResult<ReviewMessageDto> toBuyerMessageResult = getCursorResult(buyerCond, limitSize);

        ReviewMessageCondition sellerCond = ReviewMessageCondition.builder()
                .receiverId(memId)
                .reviewTargetType(ReviewTargetType.SELLER)
                .cursorId(null).build();
        CursorResult<ReviewMessageDto> toSellerMessageResult = getCursorResult(sellerCond, limitSize);

        ReviewMessageCondition allCond = ReviewMessageCondition.builder()
                .receiverId(memId)
                .reviewTargetType(ReviewTargetType.ALL)
                .cursorId(null).build();
        CursorResult<ReviewMessageDto> allMessageResult = getCursorResult(allCond, limitSize);

        Map<String, CursorResult<ReviewMessageDto>> messageMap = new HashMap<>();
        messageMap.put("toBuyerMessageResult", toBuyerMessageResult);
        messageMap.put("toSellerMessageResult", toSellerMessageResult);
        messageMap.put("allMessageResult", allMessageResult);
        return messageMap;
    }

    @Override
    public CursorResult<ReviewMessageDto> getCursorResult(ReviewMessageCondition cond,
                                                          int limitSize) {
        final List<ReviewMessageDto> messages = getReviewMessageList(cond, limitSize);
        final ReviewMessageDto lastMsg = messages.isEmpty() ? null : messages.get(messages.size() - 1);
        final boolean hasNext = hasNext(cond, lastMsg);
        final Long totalElements = getTotalElements(cond);
        return new CursorResult<>(messages, hasNext, totalElements);
    }

    private List<ReviewMessageDto> getReviewMessageList(ReviewMessageCondition cond,
                                                        int limitSize) {
        ReviewTargetType type = cond.getReviewTargetType();
        if (type.equals(ReviewTargetType.ALL)) {
            List<ReviewMessageDto> buyerMsgs = reviewBuyerRepository.findGoodMessageListByBuyerIdAndLimitAndCursorTime(cond.getReceiverId(), limitSize, cond.getCursorTime());
            List<ReviewMessageDto> sellerMsgs = reviewSellerRepository.findGoodMessageListBySellerIdAndLimitAndCursorTime(cond.getReceiverId(), limitSize, cond.getCursorTime());
            return this.concatReviewMessageListByLimit(buyerMsgs, sellerMsgs, limitSize);
        }else if (type.equals(ReviewTargetType.BUYER)) {
           return reviewBuyerRepository.findGoodMessageListByBuyerIdAndLimitAndCursorId(cond.getReceiverId(), limitSize, cond.getCursorId());
        }else if (type.equals(ReviewTargetType.SELLER)) {
            return reviewSellerRepository.findGoodMessageListBySellerIdAndLimitAndCursorId(cond.getReceiverId(), limitSize, cond.getCursorId());
        }
        return new ArrayList<>();
    }

    private boolean hasNext(ReviewMessageCondition cond, ReviewMessageDto lastMsg) {
        ReviewTargetType type = cond.getReviewTargetType();
        if (lastMsg == null) {
            return false;
        }else if (type.equals(ReviewTargetType.ALL)) {
            return reviewBuyerRepository.existsByCreatedTimeBefore(lastMsg.getCreatedTime()) ||
                    reviewSellerRepository.existsByCreatedTimeBefore(lastMsg.getCreatedTime());
        }else if (type.equals(ReviewTargetType.BUYER)) {
            return reviewBuyerRepository.existsByReviewBuyerIdLessThan(lastMsg.getReviewId());
        }else if (type.equals(ReviewTargetType.SELLER)) {
            return reviewSellerRepository.existsByReviewSellerIdLessThan(lastMsg.getReviewId());
        }
        return false;
    }

    private Long getTotalElements(ReviewMessageCondition cond) {
        ReviewTargetType type = cond.getReviewTargetType();
        if (type.equals(ReviewTargetType.ALL)) {
            return countAllGoodReviewMessages(cond.getReceiverId());
        }else if (type.equals(ReviewTargetType.BUYER)) {
            return reviewBuyerRepository.countGoodMessagesByBuyerId(cond.getReceiverId());
        }else if (type.equals(ReviewTargetType.SELLER)) {
            return reviewSellerRepository.countGoodMessagesBySellerId(cond.getReceiverId());
        }
        return 0L;
    }


    @Override
    public List<ReviewMessageDto> getAllRecentGoodReviewMessageListByLimit(String memId, int limitSize) {
        List<ReviewMessageDto> buyerMsgs = reviewBuyerRepository.findGoodMessageListByBuyerIdAndLimitAndCursorId(memId, limitSize, null);
        List<ReviewMessageDto> sellerMsgs = reviewSellerRepository.findGoodMessageListBySellerIdAndLimitAndCursorId(memId, limitSize, null);
        return this.concatReviewMessageListByLimit(buyerMsgs, sellerMsgs, limitSize);
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
