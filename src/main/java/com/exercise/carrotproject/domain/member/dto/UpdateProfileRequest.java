package com.exercise.carrotproject.domain.member.dto;

import com.exercise.carrotproject.domain.enumList.Loc;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateProfileRequest {
    private final String memId;
    private final String nickname;
    private final Loc loc;
}
