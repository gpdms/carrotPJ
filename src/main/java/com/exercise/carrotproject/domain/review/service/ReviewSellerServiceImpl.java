package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewSellerServiceImpl implements ReviewSellerService {
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;

    @Transactional
    @Override
    public void insertReviewSeller(ReviewSeller reviewSeller, List<ReviewSellerIndicator> indicatorList) {
        ReviewSeller newReviewSeller = reviewSellerRepository.save(reviewSeller);
        insertReviewSellerDetail(newReviewSeller, indicatorList);
    }
    @Transactional
    @Override
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
    @Override
    public ReviewSeller findOneReviewSeller(Long reviewSellerId){
        return reviewSellerRepository.findById(reviewSellerId)
                .orElseThrow(() -> new NoSuchElementException("reviewSeller Not Found"));
    }

    @Override
    public List<ReviewSellerIndicator> getReviewSellerIndicatorsByReview(ReviewSeller reviewSeller){
        List<ReviewSellerDetail> reviewSellerDetails = reviewSellerDetailRepository.findByReviewSeller(reviewSeller);
        return reviewSellerDetails.stream()
                .map(ReviewSellerDetail::getReviewSellerIndicator)
                .collect(Collectors.toList());
    }

    @Override
    public Long findReviewSellerIdByPost (Post post) {
        ReviewSeller reviewSeller = reviewSellerRepository.findByPost(post);
        return reviewSellerRepository.findByPost(post) != null? reviewSeller.getReviewSellerId() : 0L;
    }

    @Transactional
    @Override
    public void deleteReviewSeller(Long reviewSellerId) {
        reviewSellerRepository.deleteById(reviewSellerId);
    }

    @Transactional
    @Override
    public Map<String, String> hideReviewSeller(Long reviewBuyerId) {
        long result = reviewSellerRepository.hideReviewSellerById(reviewBuyerId);
        Map<String, String> resultMap = new HashMap<>();
        if(result>0) {
            resultMap.put("success", "숨김에 성공했습니다");
        } else {
            resultMap.put("fail", "숨김에 실패했습니다");
        }
        return resultMap;
    }
}
