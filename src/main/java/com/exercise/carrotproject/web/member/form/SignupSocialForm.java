package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class SignupSocialForm {
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,12}$",
            message = "아이디는 6~12자의 영문 대/소문자, 숫자만 사용가능합니다.")
    private String memId;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,15}$",
            message = "닉네임은 2~15자의 영문, 한글, 숫자만 사용가능합니다.")
    private String nickname;

    @NotEmpty
    private String email;

    @NotNull
    private Loc loc;

    private String profPath;
}
