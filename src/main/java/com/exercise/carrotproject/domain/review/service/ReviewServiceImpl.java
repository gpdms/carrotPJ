package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.repository.*;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewDetailCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewDetailCustomRepository reviewDetailCustomRepository;

    @Override
    public Map<ReviewIndicator, Long> getPositiveMannerDetailsBrief(String memId, long limitSize) {
        return reviewDetailCustomRepository.getMannerDetails(memId, "P")
                .stream()
                .limit(limitSize)
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf((String) row[0]),
                        row -> ((BigDecimal) row[1]).longValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    @Override
    public Map<ReviewIndicator, Long> getPositiveMannerDetails (String memId) {
      return reviewDetailCustomRepository.getMannerDetails(memId, "P")
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf((String) row[0]),
                        row -> ((BigDecimal) row[1]).longValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    @Override
    public Map<ReviewIndicator, Long> getNegativeMannerDetails (String memId) {
       return reviewDetailCustomRepository.getMannerDetails(memId, "N")
                .stream()
                .collect(Collectors.toMap(
                        row -> ReviewIndicator.valueOf((String) row[0]),
                        row -> ((BigDecimal) row[1]).longValue(),
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }
    @Override
    public Long countGoodReviewMessage(String memId) {
        return reviewBuyerRepository.countMessageByBuyer(memId) +
                reviewSellerRepository.countMessageBySeller(memId) ;
    }
    public Map<String, List<ReviewMessageDto>> goodReviewMessagesDetail(String memId) {
        List<ReviewMessageDto> buyerMessages = reviewBuyerRepository.reviewMessageByBuyer(memId);
        List<ReviewMessageDto> sellerMessages = reviewSellerRepository.reviewMessageBySeller(memId);
        List<ReviewMessageDto> allMessages = Stream.concat(buyerMessages.stream(), sellerMessages.stream())
                .sorted(Comparator.comparing(ReviewMessageDto::getCreatedTime).reversed())
                .collect(Collectors.toList());
        HashMap<String, List<ReviewMessageDto>> messageMap = new HashMap<>();
        messageMap.put("buyerMessages", buyerMessages);
        messageMap.put("sellerMessages", sellerMessages);
        messageMap.put("allMessages", allMessages);
        return messageMap;
    }
    @Override
    public List<ReviewMessageDto> goodReviewMessagesBrief(String memId, long limitSize) {
        return goodReviewMessagesDetail(memId).get("allMessages")
                .stream()
                .limit(limitSize)
                .collect(Collectors.toList());
    }
}
