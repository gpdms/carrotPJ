package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HideState {
    SHOW("0", "보임"),
    HIDE("1", "숨김");

    private String hideStateCode;
    private String hideStateName;
}
