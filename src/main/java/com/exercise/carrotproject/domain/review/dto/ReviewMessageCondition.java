package com.exercise.carrotproject.domain.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ReviewMessageCondition{
    private final String receiverId;
    private ReviewTargetType reviewTargetType;
    private Timestamp cursorTime;
    private Long cursorId;

    @Builder
    private ReviewMessageCondition(String receiverId, ReviewTargetType reviewTargetType, Long cursorId) {
        this.receiverId = receiverId;
        this.reviewTargetType = reviewTargetType;
        this.cursorId = cursorId;
    }

    private ReviewMessageCondition(String receiverId, ReviewTargetType reviewTargetType, Timestamp cursorTime) {
        this.receiverId = receiverId;
        this.reviewTargetType = reviewTargetType;
        this.cursorTime = cursorTime;
    }

    public static ReviewMessageCondition of(String receiverId, ReviewTargetType reviewTargetType, Long cursorId) {
        return new ReviewMessageCondition(receiverId, reviewTargetType, cursorId);
    }

    public static ReviewMessageCondition of(String receiverId, ReviewTargetType reviewTargetType, Timestamp cursorTime) {
        return new ReviewMessageCondition(receiverId, reviewTargetType, cursorTime);
    }

}

