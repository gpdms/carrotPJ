package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewSellerDto;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.*;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;
    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;

    private final ReviewDetailCustomRepository reviewDetailCustomRepository;

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

    public Long countGoodReviewMessage(String memId) {
        return reviewBuyerCustomRepository.countMessageByBuyer(memId) +
                reviewSellerCustomRepository.countMessageBySeller(memId) ;
    }
    public Map<String, List<ReviewMessageDto>> goodReviewMessagesDetail(String memId) {
        List<ReviewMessageDto> buyerMessages = reviewBuyerCustomRepository.reviewMessageByBuyer(memId);
        List<ReviewMessageDto> sellerMessages = reviewSellerCustomRepository.reviewMessageBySeller(memId);
        List<ReviewMessageDto> allMessages = Stream.concat(buyerMessages.stream(), sellerMessages.stream())
                .sorted(Comparator.comparing(ReviewMessageDto::getCreatedTime).reversed())
                .collect(Collectors.toList());
        HashMap<String, List<ReviewMessageDto>> messageMap = new HashMap<>();
        messageMap.put("buyerMessages", buyerMessages);
        messageMap.put("sellerMessages", sellerMessages);
        messageMap.put("allMessages", allMessages);
        return messageMap;
    }
    public List<ReviewMessageDto> goodReviewMessagesBrief(String memId, long limitSize) {
        return goodReviewMessagesDetail(memId).get("allMessages")
                .stream()
                .limit(limitSize)
                .collect(Collectors.toList());
    }
}
