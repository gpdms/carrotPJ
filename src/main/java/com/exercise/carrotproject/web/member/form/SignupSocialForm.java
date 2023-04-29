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
    @NotBlank
    private String email;

    //@NotBlank
    @Size(min=2, max = 15, message = "닉네임은 2자 이상, 15자 이하여야 합니다.")
    private String nickname;

    @NotNull
    private Loc loc;

    private String profPath;
}
