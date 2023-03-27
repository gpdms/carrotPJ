package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Loc {
    GANGDONG("1","강동"),
    GANGSEO("2","강서"),
    GANGNAM("3","강남"),
    GANGBUK("4","강북");

    //필드
    private String locCode;
    private String locName;

    public static Loc getLocNameByCode(String dbData) {
        return Arrays.stream(Loc.values())
                .filter(v -> v.getLocCode().equals(dbData))
                .findAny()
                .orElseThrow();
    }
}
