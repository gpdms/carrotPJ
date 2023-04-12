package com.exercise.carrotproject.domain.member.ouath;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KaKaoOauth {
    @Value("${kakao.login.url}")
    private String kakaoLoginUrl;
    @Value("${kakao.login.redirect.uri}")
    private String kakaoLoginRedirectUri;
    @Value("${kakao.client.id}")
    private String kakaoClientId;
    @Value("${kakao.client.secret}")
    private String kakaoClientSecret;
    @Value("${kakao.logout.redirect.uri}")
    private String kakaoLogoutRedirectUri;

    public String getKakaoLoginUrl() {
        return kakaoLoginUrl;
    }
    public String getKakaoLoginRedirectUri() {
        return kakaoLoginRedirectUri;
    }
    public String getKakaoClientId() {
        return kakaoClientId;
    }
    public String getKakaoClientSecret() {
        return kakaoClientSecret;
    }
    public String getKakaoLogoutRedirectUri() {
        return kakaoLogoutRedirectUri;
    }
}
