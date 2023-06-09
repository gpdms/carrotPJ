package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.review.dto.ReviewBuyerDto;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
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
public class ReviewBuyerServiceImpl implements ReviewBuyerService {
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    @Transactional
    @Override
    public void insertReviewBuyer(ReviewBuyer reviewBuyer, List<ReviewBuyerIndicator> indicatorList) {
        ReviewBuyer newReviewBuyer = reviewBuyerRepository.save(reviewBuyer);
        insertReviewBuyerDetail(newReviewBuyer, indicatorList);
    }
    @Transactional
    @Override
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
    @Override
    public ReviewBuyer findOneReviewBuyer(Long reviewBuyerId){
        return reviewBuyerRepository.findById(reviewBuyerId)
                .orElseThrow(() -> new NoSuchElementException("ReviewBuyer Not Found"));
    }

    @Override
    public Long findReviewBuyerIdByPost(Post post) {
        ReviewBuyer reviewBuyer = reviewBuyerRepository.findByPost(post);
        return reviewBuyerRepository.findByPost(post) != null? reviewBuyer.getReviewBuyerId() : 0L;
    }

    @Override
    public List<ReviewBuyerIndicator> getReviewBuyerIndicatorsByReview(ReviewBuyer reviewBuyer){
        List<ReviewBuyerDetail> reviewBuyerDetails = reviewBuyerDetailRepository.findByReviewBuyer(reviewBuyer);
        return reviewBuyerDetails.stream()
                .map(ReviewBuyerDetail::getReviewBuyerIndicator)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteReviewBuyer(Long reviewBuyerId) {
        ReviewBuyer oneReviewBuyer = findOneReviewBuyer(reviewBuyerId);
        reviewBuyerRepository.deleteById(reviewBuyerId);
    }

    @Transactional
    @Override
    public Map<String, String> hideReviewBuyer(Long reviewSellerId) {
        long result = reviewBuyerRepository.hideReviewBuyerById(reviewSellerId);
        Map<String, String> resultMap = new HashMap<>();
        if(result>0) {
            resultMap.put("success", "숨김에 성공했습니다");
        } else {
            resultMap.put("fail", "숨김에 성공했습니다");
        }
        return resultMap;
    }

    @Override
    public List<ReviewBuyerDto> findReviewsByPostId(List<Long> postIds) {
       return reviewBuyerRepository.getReviewIdsByPostIds(postIds);
    }
}
