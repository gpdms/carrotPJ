package com.exercise.carrotproject.web.member.form.memberInfo;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@ToString
@AllArgsConstructor (access = AccessLevel.PRIVATE)
public class ProfileForm {
    @NotNull
    private final String memId;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,15}$",
            message = "닉네임은 2~15자의 영문, 한글, 숫자만 사용가능합니다.")
    private final String nickname;

    @NotNull
    private final Loc loc;
}
