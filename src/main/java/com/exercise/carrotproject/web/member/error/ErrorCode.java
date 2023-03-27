package com.exercise.carrotproject.web.member.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_NULL("ERROR_CODE_0001","필수값이 누락되었습니다"),
    NOT_BLANK("ERROR_CODE_0002","빈 값과 공백을 허용하지 않습니다"),
    SIZE ("ERROR_CODE_0003", "사이즈가 맞지 않습니다");

    private String code;
    private String description;
}
