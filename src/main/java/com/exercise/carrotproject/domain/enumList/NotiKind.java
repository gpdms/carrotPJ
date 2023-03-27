package com.exercise.carrotproject.domain.enumList;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum NotiKind {
    ACTIVITY("1", "활동 알림"),
    KEYWORD("2", "키워드 알림");

    String notiCode;
    String notiName;

    public static NotiKind getNotiNameByCode(String dbData) {
        return Arrays.stream(NotiKind.values())
                .filter(v -> v.getNotiCode().equals(dbData))
                .findAny()
                .orElseThrow();
    }
}
