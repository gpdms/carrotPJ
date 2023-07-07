package com.exercise.carrotproject.web.member.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //Common
    INVALID_INPUT_VALUE(400,"C001", "invalid input value"),
    NO_SUCH_ELEMENT(404, "C002", "no such element"),

    //Member
    NOT_CORRECT_PWD_CONFIRM(400, "M001","not correct pwdConfirm"),
    DUPLICATED_MEM_ID(409, "M002","duplicated memId"),
    DUPLICATED_EMAIL(409, "M003","duplicated email"),
    NOT_FOUND_EMAIL(404,"M003", "not found email");

    private final int status;
    private final String divisionCode;
    private final String message;
}
