package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.repository.*;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewDetailCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId, long limitSize) {
        return reviewDetailCustomRepository.getPositiveIndicatorListByMemId(memId)
                .stream()
                .limit(limitSize)
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(row[0]),
                        row -> Long.valueOf(row[1]),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<ReviewIndicator, Long> getPositiveReviewIndicators(String memId) {
      return reviewDetailCustomRepository.getPositiveIndicatorListByMemId(memId)
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(row[0]),
                        row -> Long.valueOf(row[1]),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Map<ReviewIndicator, Long> getNegativeReviewIndicators(String memId) {
       return reviewDetailCustomRepository.getNegativeIndicatorListByMemId(memId)
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf(row[0]),
                        row -> Long.valueOf(row[1]),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    @Override
    public Long countGoodReviewMessages(String memId) {
        return reviewBuyerRepository.countGoodMessagesByBuyerId(memId)
                + reviewSellerRepository.countGoodMessagesBySellerId(memId) ;
    }

    @Override
    public Map<String, List<ReviewMessageDto>> collectGoodReviewMessages(String memId) {
        List<ReviewMessageDto> buyerMessageList = reviewBuyerRepository.getGoodMessageListByBuyerId(memId);
        List<ReviewMessageDto> sellerMessageList = reviewSellerRepository.getGoodMessageListBySellerId(memId);
        List<ReviewMessageDto> allMessageList = this.concatReviewMessageList(buyerMessageList, sellerMessageList);

        Map<String, List<ReviewMessageDto>> messageMap = new HashMap<>();
        messageMap.put("buyerMessageList", buyerMessageList);
        messageMap.put("sellerMessageList", sellerMessageList);
        messageMap.put("allMessageList", allMessageList);
        return messageMap;
    }

    @Override
    public List<ReviewMessageDto> getGoodReviewMessageListByLimit(String memId, long limitSize) {
        List<ReviewMessageDto> buyerMessageList = reviewBuyerRepository.getGoodMessageListByBuyerId(memId);
        List<ReviewMessageDto> sellerMessageList = reviewSellerRepository.getGoodMessageListBySellerId(memId);
        return this.concatReviewMessageList(buyerMessageList, sellerMessageList)
                .stream()
                .limit(limitSize)
                .collect(Collectors.toList());
    }

    private List<ReviewMessageDto> concatReviewMessageList(List<ReviewMessageDto> buyerMessageList, List<ReviewMessageDto> sellerMessageList) {
        return Stream.concat(buyerMessageList.stream(), sellerMessageList.stream())
                .sorted(Comparator.comparing(ReviewMessageDto::getCreatedTime).reversed())
                .collect(Collectors.toList());
    }
}
