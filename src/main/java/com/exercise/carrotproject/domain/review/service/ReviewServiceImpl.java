package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.dto.BuyerDetailCountDto;
import com.exercise.carrotproject.domain.review.dto.CommonDetailCountDto;
import com.exercise.carrotproject.domain.review.dto.SellerDetailCountDto;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerDetailCustomRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerDetailCustomRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final ReviewSellerDetailCustomRepository reviewSellerDetailCustomRepository;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;

    private final ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    public Map<String, Object> getPositiveMannerMap(List<BuyerDetailCountDto> buyerDetailCountList, List<SellerDetailCountDto> sellerDetailCountList ) {
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
    }

}
