package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class SignupForm {
    @Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "아이디는 영문 대/소문자이나 숫자로 6~12자로 구성되어야합니다")
    private String memId;

    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$", message ="비밀번호는 영문과 특수문자, 숫자를 포함하며 8자 이상이어야 합니다.")
    private String pwd;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private String pwdConfirm;

    @Size(min=2, max = 15, message = "닉네임은 2자 이상, 15자 이하여야 합니다.")
    private String nickname;

    @NotEmpty(message = "이메일을 입력해주세요")
    private String email;

    @NotNull
    private Loc loc;
}
