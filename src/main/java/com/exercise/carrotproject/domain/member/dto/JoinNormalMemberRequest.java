package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinNormalMemberRequest {
    private final String memId;
    private final String memPwd;
    private final String email;
    private final String nickname;
    private final Loc loc;
}
