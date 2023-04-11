package com.exercise.carrotproject.domain.member.ouath;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GoogleOauth {
    @Value("${google.auth.url}")
    private String googleAuthUrl;
    @Value("${google.login.url}")
    private String googleLoginUrl;
    @Value("${google.redirect.url}")
    private String googleRedirectUrl;
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.secret}")
    private String googleSecret;
    @Value("${google.auth.scope}")
    private String scopes;

    public String getGoogleAuthUrl() {
        return googleAuthUrl;
    }
    public String getGoogleLoginUrl() {
        return googleLoginUrl;
    }
    public String getGoogleClientId() {
        return googleClientId;
    }
    public String getGoogleRedirectUrl() {
        return googleRedirectUrl;
    }
    public String getGoogleSecret() {
        return googleSecret;
    }

    // scope의 값을 보내기 위해 띄어쓰기 값을 UTF-8로 변환하는 로직 포함
    public String getScopeUrl() {
        return scopes.replaceAll(",", "%20");
    }

}
