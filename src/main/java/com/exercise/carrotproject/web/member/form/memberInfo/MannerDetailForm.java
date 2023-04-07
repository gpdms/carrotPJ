package com.exercise.carrotproject.web.member.form.memberInfo;


import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;

import java.util.List;

public class MannerDetailForm {
    String memId;
    List<ReviewSellerIndicator> sellerIndicatorList;
    List<ReviewBuyerIndicator> buyerIndicatorList;
}
