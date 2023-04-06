package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.review.entity.ReviewBuyerDetail;
import com.exercise.carrotproject.domain.review.entity.ReviewSellerDetail;
import lombok.Data;

import java.util.List;

@Data
public class SellerDetailSearchCond {
    private String sellerId;
    private ReviewSellerIndicator sellerIndicator;
}
