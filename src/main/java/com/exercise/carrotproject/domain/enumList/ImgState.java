package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ImgState {
    NOTATTACH("0", "첨부안함"),
    ATTACH("1", "첨부");

    private String imgStateCode;
    private String imgStateName;
}
