package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerCustomRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewSellerServiceImpl {
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;

    @Transactional
    public void insertReviewSeller(ReviewSeller reviewSeller, List<ReviewSellerIndicator> indicatorList) {
        ReviewSeller newReviewSeller = reviewSellerRepository.save(reviewSeller);
        insertReviewSellerDetail(newReviewSeller, indicatorList);
    }
    @Transactional
    public void insertReviewSellerDetail(ReviewSeller newReviewSeller, List<ReviewSellerIndicator> indicatorList) {
        for (ReviewSellerIndicator reviewSellerIndicator : indicatorList) {
            ReviewSellerDetail reviewSellerDetail = ReviewSellerDetail.builder()
                    .reviewSeller(newReviewSeller)
                    .reviewSellerIndicator(reviewSellerIndicator)
                    .seller(newReviewSeller.getSeller())
                    .build();
           reviewSellerDetailRepository.save(reviewSellerDetail);
        }
    }
    public ReviewSeller findOneReviewSeller(Long reviewSellerId){
        return reviewSellerRepository.findById(reviewSellerId)
                .orElseThrow(() -> new NoSuchElementException("reviewSeller Not Found"));
    }

    public List<ReviewSellerIndicator> getReviewSellerIndicatorsByReview(ReviewSeller reviewSeller){
        List<ReviewSellerDetail> reviewSellerDetails = reviewSellerDetailRepository.findByReviewSeller(reviewSeller);
        return reviewSellerDetails.stream()
                .map(ReviewSellerDetail::getReviewSellerIndicator)
                .collect(Collectors.toList());
    }

    public Long findReviewSellerIdByPost (Post post) {
        ReviewSeller reviewSeller = reviewSellerRepository.findByPost(post);
        return reviewSellerRepository.findByPost(post) != null? reviewSeller.getReviewSellerId() : 0L;
    }

    @Transactional
    public void deleteReviewSeller(Long reviewSellerId) {
        reviewSellerRepository.deleteById(reviewSellerId);
    }

    @Transactional
    public Map<String, String> hideReviewSeller(Long reviewBuyerId) {
        long result = reviewSellerCustomRepository.hideReviewSellerById(reviewBuyerId);
        Map<String, String> resultMap = new HashMap<>();
        if(result>0) {
            resultMap.put("success", "숨김에 성공했습니다");
        } else {
            resultMap.put("fail", "숨김에 실패했습니다");
        }
        return resultMap;
    }
}
