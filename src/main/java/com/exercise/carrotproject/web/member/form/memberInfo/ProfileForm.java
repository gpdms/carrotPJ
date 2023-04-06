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

    @Size(min=2, max = 12, message = "2자 이상, 12자 이하여야 합니다.")
    private String nickname;

    @NotNull
    private Loc loc;
}
