package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.*;
import com.exercise.carrotproject.domain.review.repository.*;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewDetailCustomRepository;
import com.exercise.carrotproject.domain.review.service.message.ReviewAllMessageService;
import com.exercise.carrotproject.domain.review.service.message.ReviewBuyerMessageService;
import com.exercise.carrotproject.domain.review.service.message.ReviewMessageFactory;
import com.exercise.carrotproject.domain.review.service.message.ReviewMessageService;
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
    private final ReviewMessageFactory reviewMessageFactory;

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
        CursorResult<ReviewMessageDto> toBuyerMessageResult = getReviewMessageCursorResult(buyerCond, limitSize);

        ReviewMessageCondition sellerCond = ReviewMessageCondition.builder()
                .receiverId(memId)
                .reviewTargetType(ReviewTargetType.SELLER)
                .cursorId(null).build();
        CursorResult<ReviewMessageDto> toSellerMessageResult = getReviewMessageCursorResult(sellerCond, limitSize);

        ReviewMessageCondition allCond = ReviewMessageCondition.builder()
                .receiverId(memId)
                .reviewTargetType(ReviewTargetType.ALL)
                .cursorId(null).build();
        CursorResult<ReviewMessageDto> allMessageResult = getReviewMessageCursorResult(allCond, limitSize);

        Map<String, CursorResult<ReviewMessageDto>> messageMap = new HashMap<>();
        messageMap.put("toBuyerMessageResult", toBuyerMessageResult);
        messageMap.put("toSellerMessageResult", toSellerMessageResult);
        messageMap.put("allMessageResult", allMessageResult);
        return messageMap;
    }

    @Override
    public CursorResult<ReviewMessageDto> getReviewMessageCursorResult(ReviewMessageCondition cond,
                                                          int limitSize) {
        ReviewMessageService reviewMessageService = reviewMessageFactory.find(cond.getReviewTargetType());
        return reviewMessageService.getCursorResult(cond, limitSize);
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
