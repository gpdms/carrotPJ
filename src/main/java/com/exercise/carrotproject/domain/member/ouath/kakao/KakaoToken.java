package com.exercise.carrotproject.domain.member.ouath.kakao;


import com.exercise.carrotproject.domain.member.ouath.OauthToken;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoToken extends OauthToken {
    private String refresh_token_expires_in;

    KakaoToken(String access_token, String token_type, String refresh_token, String expires_in, String refresh_token_expires_in) {
        super(access_token, token_type, refresh_token, expires_in);
        this.refresh_token_expires_in = refresh_token_expires_in;
    }
}
