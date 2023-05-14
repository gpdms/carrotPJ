package com.exercise.carrotproject.web.member.form;

import lombok.Data;

import javax.validation.constraints.Pattern;


@Data
public class EmailRequestForm {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식에 맞지 않습니다")
    public String email;
}

