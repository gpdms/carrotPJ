package com.exercise.carrotproject.domain.member.dto;


import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberDto {
    private final String memId;
    private String nickname;
    private Loc loc;
    private final Double mannerScore;
    private Role role;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setLoc(Loc loc) {
        this.loc = loc;
    }
}
