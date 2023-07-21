package com.exercise.carrotproject.domain.review.dto;

import lombok.*;

import java.util.List;


@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddReviewRequest {
    private final Long postId;
    private final String sellerId;
    private final String buyerId;
    private final Double totalScore;
    private final String message;
    private final String reviewStateCode;
    private final List<String> indicatorNames;
}
