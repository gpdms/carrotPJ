package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.dto.ReviewSellerDto;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerDetailCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerDetailCustomRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final ReviewSellerDetailCustomRepository reviewSellerDetailCustomRepository;
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;

    private final ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;
    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

/*    public Map<String, Object> getPositiveMannerMap(List<BuyerDetailCountDto> buyerDetailCountList, List<SellerDetailCountDto> sellerDetailCountList ) {
        Map<String, Object> positiveMannerMap = new HashMap<>();
        if (buyerDetailCountList != null) {
            positiveMannerMap.put("buyer", buyerDetailCountList.stream().filter(
                            buyer -> buyer.getReviewBuyerIndicator().name().contains("PB"))
                    .collect(Collectors.toList()));
        }
        if (sellerDetailCountList != null) {
            positiveMannerMap.put("seller", sellerDetailCountList.stream().filter(
                            seller -> seller.getReviewSellerIndicator().name().contains("PS"))
                    .collect(Collectors.toList()));
        }
        if(buyerDetailCountList != null && sellerDetailCountList != null) {
            List<SellerDetailCountDto> collect = buyerDetailCountList.stream()
                    .filter(buyer -> buyer.getReviewBuyerIndicator().name().contains("P"))
                    .filter(buyer -> !buyer.getReviewBuyerIndicator().name().contains("B"))
                    .flatMap(buyer -> sellerDetailCountList.stream().filter(
                            seller -> buyer.getReviewBuyerIndicator().name().equals(seller.getReviewSellerIndicator().name())
                    )).collect(Collectors.toList());
            positiveMannerMap.put("common", collect);
        }
        return positiveMannerMap;
    }
    public Map<String,Object> getNegativeMannerMap(List<BuyerDetailCountDto> buyerDetailCountList, List<SellerDetailCountDto> sellerDetailCountList) {
        Map<String, Object> negativeMannerMap = new HashMap<>();
        if (buyerDetailCountList != null) {
            negativeMannerMap.put("buyer", buyerDetailCountList.stream()
                    .filter(buyerOne -> buyerOne.getReviewBuyerIndicator().name().contains("NB"))
                    .collect(Collectors.toList()));
        }
        if (sellerDetailCountList != null) {
            negativeMannerMap.put("seller", sellerDetailCountList.stream().filter(
                            sellerOne -> sellerOne.getReviewSellerIndicator().name().contains("NS"))
                    .collect(Collectors.toList()));
        }
        if(buyerDetailCountList!= null && sellerDetailCountList != null) {
            List<SellerDetailCountDto> collect = buyerDetailCountList.stream()
                    .filter(buyer -> buyer.getReviewBuyerIndicator().name().contains("N"))
                    .filter(buyer -> !buyer.getReviewBuyerIndicator().name().contains("B"))
                    .flatMap(buyer -> sellerDetailCountList.stream().filter(
                            seller -> buyer.getReviewBuyerIndicator().name().equals(seller.getReviewSellerIndicator().name())
                    )).collect(Collectors.toList());
            negativeMannerMap.put("common", collect);
        }
        return negativeMannerMap;
    }*/

    public Map<String, Map<Object, Long>> getMannerDetailMap(String memId) {
        Map<ReviewBuyerIndicator, Long> sortedBuyerIndicatorMap = sortBuyerIndicatorCount(memId);
        Map<ReviewSellerIndicator, Long> sortedSellerIndicatorMap = sortSellerIndicatorCount(memId);
        Map<Enum, Long> commonIndicatorMap
                = commonIndicatorCount(sortedBuyerIndicatorMap, sortedSellerIndicatorMap);
        Map<String, Map<Object, Long>> mannerDetailMap= new HashMap<>();
        mannerDetailMap.put("positiveMannerMap", getPositiveMannerMap(sortedBuyerIndicatorMap, sortedSellerIndicatorMap, commonIndicatorMap));
        mannerDetailMap.put("negativeMannerMap", getNegativeMannerMap(sortedBuyerIndicatorMap, sortedSellerIndicatorMap, commonIndicatorMap));
        return mannerDetailMap;
    }
    public Map<Object, Long> getPositiveMannerBrief(String memId, long limitSize) {
        Map<ReviewBuyerIndicator, Long> sortedBuyerIndicatorMap = sortBuyerIndicatorCount(memId);
        Map<ReviewSellerIndicator, Long> sortedSellerIndicatorMap = sortSellerIndicatorCount(memId);
        Map<Enum, Long> commonIndicatorMap
                = commonIndicatorCount(sortedBuyerIndicatorMap, sortedSellerIndicatorMap);
        Map<Object, Long> positiveMannerMap = getPositiveMannerMap(sortedBuyerIndicatorMap, sortedSellerIndicatorMap, commonIndicatorMap);
        Map<Object, Long> collect = positiveMannerMap
                .entrySet().stream()
                .limit(limitSize)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        System.out.println("collect = " + collect);
        return collect;
    }

    public Map<ReviewBuyerIndicator, Long> sortBuyerIndicatorCount(String memId) {
        return reviewBuyerDetailCustomRepository.countBuyerIndicatorByBuyer(memId)
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().toString()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }
    public Map<ReviewSellerIndicator, Long> sortSellerIndicatorCount(String memId) {
        return reviewSellerDetailCustomRepository.countSellerIndicatorBySeller(memId)
                .entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getKey().toString()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }
    public Map<Enum, Long> commonIndicatorCount(Map<ReviewBuyerIndicator, Long> buyerIndicatorMap, Map<ReviewSellerIndicator, Long> sellerIndicatorMap){
        Map<Enum, Long> commonMap = Stream.concat(buyerIndicatorMap.entrySet().stream(), sellerIndicatorMap.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, LinkedHashMap::new, Collectors.summingLong(Map.Entry::getValue)));
        commonMap.entrySet().removeIf(entry->entry.getKey().name().contains("B"));
        commonMap.entrySet().removeIf(entry->entry.getKey().name().contains("S"));
        System.out.println("commonMap = " + commonMap);
        return commonMap;
    }
    public Map<Object, Long> getPositiveMannerMap(Map<ReviewBuyerIndicator, Long> buyerIndicatorMap, Map<ReviewSellerIndicator, Long> sellerIndicatorMap, Map<Enum, Long> commonIndicatorMap) {
        Map<Object, Long> positiveMap = new LinkedHashMap<>();
        commonIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("P"))
                .forEach(entry -> positiveMap.put(entry.getKey(), entry.getValue()));
        buyerIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("PB"))
                .forEach(entry -> positiveMap.put(entry.getKey(), entry.getValue()));
        sellerIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("PS"))
                .forEach(entry -> positiveMap.put(entry.getKey(), entry.getValue()));
        return positiveMap;
    }
    public Map<Object, Long> getNegativeMannerMap(Map<ReviewBuyerIndicator, Long> buyerIndicatorMap, Map<ReviewSellerIndicator, Long> sellerIndicatorMap, Map<Enum, Long> commonIndicatorMap) {
        Map<Object, Long> negativeMap = new LinkedHashMap<>();
        commonIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("N"))
                .forEach(entry -> negativeMap.put(entry.getKey(), entry.getValue()));
        buyerIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("NB"))
                .forEach(entry -> negativeMap.put(entry.getKey(), entry.getValue()));
        sellerIndicatorMap.entrySet().stream()
                .filter(entry -> entry.getKey().name().contains("NS"))
                .forEach(entry -> negativeMap.put(entry.getKey(), entry.getValue()));
        return negativeMap;
    }

    public Map<String, Long> countGoodReviewMessage(String memId) {
        Map<String, Long> countMap= new HashMap<>();
        countMap.put("buyerCount", reviewBuyerCustomRepository.countMessageByBuyer(memId));
        countMap.put("sellerCount", reviewSellerCustomRepository.countMessageBySeller(memId));
        countMap.put("AllCount",countMap.get("reviewBuyer")+countMap.get("reviewSeller"));
        return countMap;
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
