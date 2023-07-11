package com.exercise.carrotproject.web.member.form;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
public class EmailRequestForm {
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식이 올바르지 않습니다.")
    public final String email;

    @JsonCreator
    protected EmailRequestForm(@JsonProperty("email") String email) {
        this.email = email;
    }
}

