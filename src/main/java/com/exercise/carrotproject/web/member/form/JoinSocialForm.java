package com.exercise.carrotproject.web.member.form;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@ToString
public class JoinSocialForm {
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,15}$",
            message = "닉네임은 2~15자의 영문, 한글, 숫자만 사용가능합니다.")
    private final String nickname;
    @NotEmpty
    private final String email;
    @NotNull
    private final Loc loc;
    private final String profImgUrl;

    @JsonCreator
    protected JoinSocialForm(@JsonProperty("nickname") String nickname,
                             @JsonProperty("email") String email,
                             @JsonProperty("loc") Loc loc,
                             @JsonProperty("profileImgUrl") String profileImgUrl) {
        this.nickname = nickname;
        this.email = email;
        this.loc = loc;
        this.profImgUrl = profileImgUrl;
    }
}
