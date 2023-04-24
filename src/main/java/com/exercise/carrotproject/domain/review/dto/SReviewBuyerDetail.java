package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator;
import com.exercise.carrotproject.domain.member.entity.QMember;
import com.exercise.carrotproject.domain.review.entity.QReviewBuyerDetail;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;

public class SReviewBuyerDetail extends RelationalPathBase<QReviewBuyerDetail> {
    public SReviewBuyerDetail(String path) {
        super(QReviewBuyerDetail.class, PathMetadataFactory.forVariable(path), "", "review_buyer_detail");
    }
    public final EnumPath<ReviewBuyerIndicator> reviewBuyerIndicator = createEnum("review_buyer_indicator", com.exercise.carrotproject.domain.enumList.ReviewBuyerIndicator.class);
}
