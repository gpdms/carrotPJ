package com.exercise.carrotproject.domain.member.ouath;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

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

    public ResponseEntity<String> requestAccessToken(String code) {
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate=new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", googleClientId);
        params.put("client_secret", googleSecret);
        params.put("redirect_uri", googleRedirectUrl);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity= restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params, String.class);

        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity;
        }
        return null;
    }
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
