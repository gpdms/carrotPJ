package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.BuyListRepository;
import com.exercise.carrotproject.domain.review.dto.BuyerDetailCountDto;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
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
public class ReviewBuyerServiceImpl {
    private final ReviewBuyerDetailCustomRepository reviewBuyerDetailCustomRepository;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    private final BuyListRepository buyListRepository;

    @Transactional
    public void insertReviewBuyer(ReviewBuyer reviewBuyer, List<ReviewBuyerIndicator> indicatorList) {
        ReviewBuyer newReviewBuyer = reviewBuyerRepository.save(reviewBuyer);
        insertReviewBuyerDetail(newReviewBuyer, indicatorList);
        //구매자에 대한 리뷰등록시 상대편의 구매목록에 추가
        BuyList buyList = BuyList.builder().post(reviewBuyer.getPost())
                .buyer(reviewBuyer.getBuyer())
                .seller(reviewBuyer.getSeller()).build();
        buyListRepository.save(buyList);
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
        reviewBuyerRepository.findById(reviewBuyerId);
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

    @Transactional
    public void deleteReviewBuyer(Long reviewBuyerId) {
        //구매자에 대한 리뷰 삭제시, 상대편의 구매목록에서도 삭제
        ReviewBuyer oneReviewBuyer = findOneReviewBuyer(reviewBuyerId);
        buyListRepository.deleteByPost(oneReviewBuyer.getPost());
        reviewBuyerRepository.deleteById(reviewBuyerId);
    }

    public List<BuyerDetailCountDto> buyerIndicatorsForMannerDetail(String memId) {
        return reviewBuyerDetailCustomRepository.countIndicatorByBuyer(memId);
    }


}
