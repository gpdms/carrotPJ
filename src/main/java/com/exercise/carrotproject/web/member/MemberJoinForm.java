package com.exercise.carrotproject.web.member;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;

@Data
public class MemberJoinForm {
    @NotBlank
    @Range(min = 6, max = 12)
    private String memId;

    @NotBlank
    @Min(8)
    private String memPwd;

    @Max(12)
    private String nickname;

    @NotNull
    private Loc loc;

}
