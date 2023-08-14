package com.exercise.carrotproject.domain.member.ouath.naver;


import com.exercise.carrotproject.domain.member.ouath.OauthToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverToken extends OauthToken {
    private String error;
    private String error_description;

    NaverToken(String access_token, String token_type, String refresh_token, String expires_in, String error, String error_description) {
        super(access_token, token_type, refresh_token, expires_in);
        this.error = error;
        this.error_description = error_description;
    }
}
