package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
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

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReviewBuyerServiceImpl {
    private final ReviewBuyerCustomRepository reviewBuyerCustomRepository;
    private final ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;

    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;


    @Transactional
    public void insertReviewBuyer(ReviewBuyer reviewBuyer, List<ReviewBuyerIndicator> indicatorList) {
        ReviewBuyer newReviewBuyer = reviewBuyerRepository.save(reviewBuyer);
        insertReviewBuyerDetail(newReviewBuyer, indicatorList);
    }
    @Transactional
    public void insertReviewBuyerDetail(ReviewBuyer newReviewBuyer, List<ReviewBuyerIndicator> indicatorList) {
        for (ReviewBuyerIndicator reviewBuyerIndicator : indicatorList) {
            ReviewBuyerDetail reviewBuyerDetail = ReviewBuyerDetail.builder()
                    .reviewBuyer(newReviewBuyer)
                    .reviewBuyerIndicator(reviewBuyerIndicator)
                    .buyer(newReviewBuyer.getBuyer())
                    .build();
            reviewBuyerDetailRepository.save(reviewBuyerDetail);
        }
    }
    public ReviewBuyer findOneReviewBuyer(Long reviewBuyerId){
        return reviewBuyerRepository.findById(reviewBuyerId)
                .orElseThrow(() -> new NoSuchElementException("reviewBuyer Not Found"));
    }

    public Long findReviewBuyerIdByPost(Post post) {
        ReviewBuyer reviewBuyer = reviewBuyerRepository.findByPost(post);
        return reviewBuyerRepository.findByPost(post) != null? reviewBuyer.getReviewBuyerId() : 0L;
    }

    public List<ReviewBuyerIndicator> getReviewBuyerIndicatorsByReview(ReviewBuyer reviewBuyer){
        List<ReviewBuyerDetail> reviewBuyerDetails = reviewBuyerDetailRepository.findByReviewBuyer(reviewBuyer);
        return reviewBuyerDetails.stream()
                .map(ReviewBuyerDetail::getReviewBuyerIndicator)
                .collect(Collectors.toList());
    }
//    public Map<ReviewSellerIndicator, Long> () {
//
//    }

}
