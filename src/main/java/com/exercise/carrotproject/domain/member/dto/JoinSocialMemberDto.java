package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.util.GenerateUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinSocialMemberDto {
    private final String email;
    private final String nickname;
    private final Loc loc;
    private final Role role;
    private final String profImgUrl;
}
