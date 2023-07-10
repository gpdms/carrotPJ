package com.exercise.carrotproject.domain.member.dto;


import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.sql.Timestamp;
import java.util.Date;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class MemberDto {
    private final String memId;
    private String nickname;
    private String email;
    private Loc loc;
    private String profPath;
    private final Double mannerScore;
    private Role role;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Date updatedTimeManner;
    private String memPwd;

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setLoc(Loc loc) {
        this.loc = loc;
    }
}
