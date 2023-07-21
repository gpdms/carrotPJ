package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinSocialMemberRequest {
    private final String email;
    private final String nickname;
    private final Loc loc;
    private final Role role;
    private final String profImgUrl;
}
