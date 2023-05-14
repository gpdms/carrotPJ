package com.exercise.carrotproject.web.member.form.memberInfo;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class PwdUpdateForm {
    @NotNull
    private String memId;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message ="비밀번호는 영문과 특수문자, 숫자를 포함하며 8자 이상이어야 합니다.")
    private String pwd;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String pwdConfirm;
}
