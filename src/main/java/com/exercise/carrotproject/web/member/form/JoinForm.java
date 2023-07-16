package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.*;

@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinForm {
    @Pattern(regexp = "^(?=.*[a-zA-Z])[a-zA-Z0-9]{6,12}$",
            message = "아이디는 6~12자의 영문 대/소문자, 숫자만 사용가능합니다.")
    private final String memId;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$"
            , message ="비밀번호는 8~16자의 영문, 특수문자, 숫자를 사용해야합니다.")
    private final String pwd;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    private final String pwdConfirm;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,15}$",
            message = "닉네임은 2~15자의 영문, 한글, 숫자만 사용가능합니다.")
    private final String nickname;

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "이메일 형식이 올바르지 않습니다.")
    private final String email;

    @NotNull
    private final Loc loc;
}
