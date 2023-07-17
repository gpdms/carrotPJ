package com.exercise.carrotproject.web.member.form.memberInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class PwdUpdateForm {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$"
            , message ="비밀번호는 8~16자의 영문, 특수문자, 숫자를 사용해야합니다.")
    private final String pwd;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private final String pwdConfirm;

    @JsonCreator
    public PwdUpdateForm(@JsonProperty("pwd") String pwd, @JsonProperty("pwdConfirm") String pwdConfirm) {
        this.pwd = pwd;
        this.pwdConfirm = pwdConfirm;
    }
}
