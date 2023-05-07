package com.exercise.carrotproject.web.member.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_NULL("ERROR_CODE_0001","필수값이 누락되었습니다"),
    NOT_BLANK("ERROR_CODE_0002","빈 값과 공백을 허용하지 않습니다"),
    SIZE ("ERROR_CODE_0003", "크기가 맞지 않습니다"),
    PATTERN ("ERROR_CODE_0004", "패턴이 맞지 않습니다"),

    NO_SUCH_ELEMENT ("ERROR_CODE_0010", "DB에 없는 데이터입니다.");

    private String code;
    private String description;
}
