package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.service.TradeService;
import com.exercise.carrotproject.domain.review.dto.AddReviewRequest;
import com.exercise.carrotproject.domain.review.dto.ReviewResponse;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewSellerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewSellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewSellerServiceImpl implements ReviewSellerService {
    private final TradeService tradeService;
    private final ReviewSellerRepository reviewSellerRepository;
    private final ReviewSellerDetailRepository reviewSellerDetailRepository;

    @Override
    public ReviewSeller findReviewSellerById(Long reviewSellerId){
        return reviewSellerRepository.findById(reviewSellerId)
                .orElseThrow(() -> new NoSuchElementException("ReviewSeller Not Found"));
    }

    @Override
    public ReviewResponse getReviewResponseById(Long reviewSellerId) {
        ReviewSeller reviewSeller = findReviewSellerById(reviewSellerId);
        return ReviewResponse.of(reviewSeller);
    }

    @Override
    public boolean existsReviewSellerByPostId(Long postId) {
        return reviewSellerRepository.existsByPostPostId(postId);
    }

    @Transactional
    @Override
    public Long insertReviewSeller(AddReviewRequest req) {
        ReviewSeller reviewSeller = this.createReviewSeller(req);
        ReviewSeller newReviewSeller = reviewSellerRepository.save(reviewSeller);

        List<ReviewSellerDetail> reviewSellerDetails = this.createReviewSellerDetailList(newReviewSeller, req.getIndicatorNames());
        reviewSellerDetailRepository.saveAll(reviewSellerDetails);

        return newReviewSeller.getReviewSellerId();
    }

    private ReviewSeller createReviewSeller(AddReviewRequest req) {
        Trade trade = tradeService.findTradeByPostId(req.getPostId());
        List<ReviewSellerIndicator> indicatorList = ReviewSellerIndicator
                .findAllByEnumName(req.getIndicatorNames());
        String message = req.getMessage().replace("\r\n", "<br>");
        return ReviewSeller.builder()
                .seller(trade.getSeller())
                .buyer(trade.getBuyer())
                .post(trade.getPost())
                .reviewState(ReviewState.findByStateCode(req.getReviewStateCode()))
                .totalScore(ReviewSellerIndicator.sumScore(indicatorList))
                .message(message)
                .build();
    }

    private List<ReviewSellerDetail> createReviewSellerDetailList(ReviewSeller newReviewSeller,
                                                                  List<String> indicatorNames) {
        List<ReviewSellerIndicator> indicatorList = ReviewSellerIndicator.findAllByEnumName(indicatorNames);
        return indicatorList.stream()
                .map(reviewSellerIndicator -> ReviewSellerDetail.builder()
                        .reviewSeller(newReviewSeller)
                        .reviewSellerIndicator(reviewSellerIndicator)
                        .seller(newReviewSeller.getSeller())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteReviewSellerById(Long reviewSellerId) {
        reviewSellerRepository.deleteById(reviewSellerId);
    }

    @Transactional
    @Override
    public void hideReviewSeller(Long reviewSellerId) {
        ReviewSeller reviewSeller = findReviewSellerById(reviewSellerId);
        reviewSeller.updateHideState(HideState.HIDE);
    }
}
