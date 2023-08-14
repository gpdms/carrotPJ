package com.exercise.carrotproject.domain.member.ouath.naver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "naver.oauth")
@ConstructorBinding
public class NaverOauth {
    private final String baseUrl;
    private final String loginRedirectUri;
    private final String clientId;
    private final String clientSecret;
}
