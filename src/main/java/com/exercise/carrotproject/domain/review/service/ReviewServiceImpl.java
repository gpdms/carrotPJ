package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl {
    private final ReviewSellerCustomRepository reviewSellerCustomRepository;
    private final ReviewSellerDetailCustomRepository reviewSellerDetailCustomRepository;
    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;
    private final ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;

    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;


    @Transactional
    public void insertReviewSeller(ReviewSeller reviewSeller, List<ReviewSellerIndicator> indicatorList) {
        ReviewSeller newReviewSeller = reviewSellerRepository.save(reviewSeller);
        insertReviewSellerDetail(newReviewSeller, indicatorList);
    }

    @Transactional
    public void insertReviewSellerDetail(ReviewSeller newReviewSeller, List<ReviewSellerIndicator> indicatorList) {
        for (ReviewSellerIndicator reviewSellerIndicator : indicatorList) {
            ReviewSellerDetail reviewSellerDetail = ReviewSellerDetail.builder().reviewSeller(newReviewSeller)
                    .reviewSellerIndicator(reviewSellerIndicator)
                    .seller(newReviewSeller.getSeller())
                    .build();
           reviewSellerDetailRepository.save(reviewSellerDetail);
        }
    }
    public boolean isSellerReviewRegistered (Post post) {
        return reviewSellerRepository.findByPost(post) == null? true : false;
    }
    public boolean isBuyerReviewRegistered(Post post) {
        return reviewBuyerRepository.findByPost(post) == null? true : false;
    }
//    public Map<ReviewSellerIndicator, Long> () {
//
//    }
}
