package com.exercise.carrotproject.domain.review.service;


import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.review.entity.ReviewSeller;
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

import java.util.List;


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

    public void insertReviewSeller() {
    }

}
