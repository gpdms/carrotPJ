package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum ReadState {
    NOTREAD("0", "읽지않음"),
    READ("1", "읽음");

    private String readStateCode;
    private String readStateName;
}
