package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import javax.validation.constraints.*;

@Getter
@Setter
@ToString
public class ProfileForm {
    @NotNull
    private String memId;

    @ColumnDefault("'D:/pf/profile_img.png'")
    @Size(max=1000)
    private String profPath;

    @Size(max = 12, message = "12자 이하여야 합니다.")
    private String nickname;

    @NotNull
    private Loc loc;
}
