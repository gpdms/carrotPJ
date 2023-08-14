package com.exercise.carrotproject.domain.member.ouath.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "kakao.oauth")
@ConstructorBinding
public class KaKaoOauth {
    private final String baseUrl;
    private final String loginRedirectUri;
    private final String clientId;
    private final String clientSecret;
    private final String logoutRedirectUri;
}

