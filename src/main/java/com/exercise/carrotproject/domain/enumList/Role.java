package com.exercise.carrotproject.domain.enumList;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN,
    NORMAL,
    SOCIAL_KAKAO,
    SOCIAL_NAVER
}
