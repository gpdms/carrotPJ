package com.exercise.carrotproject.domain.member.ouath;

import lombok.Getter;

@Getter
public class OauthCallback {
    private final String code;
    private final String state;
    private final String error;
    private final String errorDescription;

    public OauthCallback(String code, String state, String error, String error_description) {
        this.code = code;
        this.state = state;
        this.error = error;
        this.errorDescription = error_description;
    }
}
