package com.exercise.carrotproject.web.member.form.memberInfo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class PwdUpdateForm {
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$"
            , message ="비밀번호는 8~16자의 영문, 특수문자, 숫자를 사용해야합니다.")
    private String pwd;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String pwdConfirm;
}
