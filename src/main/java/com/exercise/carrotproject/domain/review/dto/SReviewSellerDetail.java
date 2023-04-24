package com.exercise.carrotproject.domain.review.dto;

import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator;
import com.exercise.carrotproject.domain.member.entity.QMember;
import com.exercise.carrotproject.domain.review.entity.QReviewSellerDetail;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.RelationalPathBase;

public class SReviewSellerDetail extends RelationalPathBase<QReviewSellerDetail> {
    public SReviewSellerDetail(String path) {
        super(QReviewSellerDetail.class, PathMetadataFactory.forVariable(path), "", "review_seller_detail");
    }
    public final EnumPath<ReviewSellerIndicator> reviewSellerIndicator = createEnum("review_seller_indicator", com.exercise.carrotproject.domain.enumList.ReviewSellerIndicator.class);
}
