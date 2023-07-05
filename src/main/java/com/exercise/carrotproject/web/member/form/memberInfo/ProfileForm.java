package com.exercise.carrotproject.web.member.form.memberInfo;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfileForm {
    @NotNull
    private String memId;

    @Size(max=1000)
    private String profPath;

    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,15}$",
            message = "닉네임은 2~15자의 영문, 한글, 숫자만 사용가능합니다.")
    private String nickname;

    @NotNull
    private Loc loc;
}
