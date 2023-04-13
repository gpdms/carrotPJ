package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Loc {
    GANGDONG("1","강동",37.53018250762979,127.12379248338428),
    GANGSEO("2","강서",37.55095740746902,126.84953377400353),
    GANGNAM("3","강남", 37.517328323662205,127.04737288147676),
    GANGBUK("4","강북", 37.63974411971604,127.02553806938644);

    //필드
    private String locCode;
    private String locName;

    private Double lat;
    private Double lon;

    public static Loc getLocNameByCode(String dbData) {
        return Arrays.stream(Loc.values())
                .filter(v -> v.getLocCode().equals(dbData))
                .findAny()
                .orElseThrow();
    }

}
