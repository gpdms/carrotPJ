package com.exercise.carrotproject.web.member.form;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter
@ToString
@AllArgsConstructor
public class LoginForm {
    @NotEmpty
    private final String loginId;
    @NotEmpty
    private final String pwd;
}
