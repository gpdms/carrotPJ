package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import lombok.Data;

import java.util.List;

@Data
public class BuyerDetailSearchCond {
    private String buyerId;
    private ReviewBuyerIndicator buyerIndicator;
}
