package com.exercise.carrotproject.web.member;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class LoginForm {
    @NotEmpty
    private String loginId;
    @NotEmpty
    private String pwd;
}
