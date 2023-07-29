package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.enumList.HideState;
import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewState;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.service.TradeService;
import com.exercise.carrotproject.domain.review.dto.AddReviewRequest;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyer;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.repository.detail.ReviewBuyerDetailRepository;
import com.exercise.carrotproject.domain.review.repository.ReviewBuyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReviewBuyerServiceImpl implements ReviewBuyerService {
    private final TradeService tradeService;
    private final ReviewBuyerRepository reviewBuyerRepository;
    private final ReviewBuyerDetailRepository reviewBuyerDetailRepository;

    @Override
    public ReviewBuyer findReviewBuyerById(Long reviewBuyerId){
        return reviewBuyerRepository.findById(reviewBuyerId)
                .orElseThrow(() -> new NoSuchElementException("ReviewBuyer Not Found"));
    }

    @Override
    public boolean existsReviewBuyerByPostId(Long postId) {
        return reviewBuyerRepository.existsByPostPostId(postId);
    }

    @Transactional
    @Override
    public Long insertReviewBuyer(AddReviewRequest req) {
        ReviewBuyer reviewBuyer = processReviewBuyer(req);
        ReviewBuyer newReviewBuyer = reviewBuyerRepository.save(reviewBuyer);

        List<ReviewBuyerDetail> reviewBuyerDetailList = processReviewBuyerDetailList(newReviewBuyer, req.getIndicatorNames());
        reviewBuyerDetailRepository.saveAll(reviewBuyerDetailList);

        return newReviewBuyer.getReviewBuyerId();
    }

    private ReviewBuyer processReviewBuyer(AddReviewRequest req) {
        Trade trade = tradeService.findTradeByPostId(req.getPostId());
        List<ReviewBuyerIndicator> indicatorList = ReviewBuyerIndicator.findAllByEnumName(req.getIndicatorNames());
        String message = req.getMessage().replace("\r\n", "<br>");
        return ReviewBuyer.builder()
                .seller(trade.getSeller())
                .buyer(trade.getBuyer())
                .post(trade.getPost())
                .reviewState(ReviewState.findByStateCode(req.getReviewStateCode()))
                .totalScore(ReviewBuyerIndicator.sumScore(indicatorList))
                .message(message)
                .build();
    }

   private List<ReviewBuyerDetail> processReviewBuyerDetailList(ReviewBuyer newReviewBuyer,
                                                                List<String> indicatorNames) {
       List<ReviewBuyerIndicator> indicators = ReviewBuyerIndicator.findAllByEnumName(indicatorNames);
        return indicators.stream()
               .map(reviewBuyerIndicator -> ReviewBuyerDetail.builder()
                       .reviewBuyer(newReviewBuyer)
                       .reviewBuyerIndicator(reviewBuyerIndicator)
                       .buyer(newReviewBuyer.getBuyer())
                       .build())
               .collect(Collectors.toList());
   }

    @Transactional
    @Override
    public void deleteReviewBuyerById(Long reviewBuyerId) {
        reviewBuyerRepository.deleteById(reviewBuyerId);
    }

    @Transactional
    @Override
    public void hideReviewBuyer(Long reviewBuyerId) {
        ReviewBuyer reviewBuyer = findReviewBuyerById(reviewBuyerId);
        reviewBuyer.updateHideState(HideState.HIDE);
    }
}
