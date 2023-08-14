package com.exercise.carrotproject.domain.review.service.message;

import com.exercise.carrotproject.domain.review.dto.ReviewTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ReviewMessageFactory {
    private final List<ReviewMessageService> reviewMessageServiceList;
    private final Map<ReviewTargetType, ReviewMessageService> factoryCache;

    public ReviewMessageService find(final ReviewTargetType reviewTargetType) {
        ReviewMessageService reviewMessageService = factoryCache.get(reviewTargetType);
        if(reviewMessageService != null) {
            return reviewMessageService;
        }

        reviewMessageService = reviewMessageServiceList.stream()
                .filter(s -> s.supports(reviewTargetType))
                .findFirst()
                .orElseThrow();
        factoryCache.put(reviewTargetType, reviewMessageService);

        return reviewMessageService;
    }
}
