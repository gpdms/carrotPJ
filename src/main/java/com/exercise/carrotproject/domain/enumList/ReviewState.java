package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ReviewState {
    BAD(0, "별로에요.", false, -1.0),
    GOOD(1, "좋아요.", true, 0.0),
    BEST(2, "최고에요.", true,1.0);

    private final Integer stateCode;
    private final String description;
    private final boolean access;
    private final Double score;

}
