package com.exercise.carrotproject.domain.common.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HideState {
    SHOW(0, "보임"),
    HIDE(1, "숨김");

    private Integer hideStateCode;
    private String hideStateName;
}
