package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SellState {
    ON_SALE("0","판매중"),
    RESERVATION("1","예약중"),
    SOLD("2","판매완료");

    private String sellStateCode;
    private String sellStateName;
}
