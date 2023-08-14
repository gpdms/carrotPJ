package com.exercise.carrotproject.domain.member.ouath.naver;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.ouath.OauthCallback;
import com.exercise.carrotproject.domain.member.ouath.OauthService;
import com.exercise.carrotproject.domain.member.ouath.OauthToken;
import com.exercise.carrotproject.domain.member.ouath.OauthUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
public class NaverOauthService implements OauthService {
    private final NaverOauth naverOauth;
    private static final String NAVER_USER_INFO_REQ_URL = "https://openapi.naver.com/v1/nid/me";

    @Override
    public boolean supports(Role role) {
        return role == Role.SOCIAL_NAVER;
    }

    @Override
    public String getAuthorizeUrl() {
        UriComponents uriComponents = null;
        try {
            uriComponents = UriComponentsBuilder
                    .fromUriString(naverOauth.getBaseUrl() + "/authorize")
                    .queryParam("client_id", naverOauth.getClientId())
                    .queryParam("redirect_uri", URLEncoder.encode(naverOauth.getLoginRedirectUri(),"UTF-8"))
                    .queryParam("state", URLEncoder.encode("naver-login", "UTF-8"))
                    .queryParam("response_type", "code")
                    .build();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return uriComponents.toString();
    }

    @Override
    public OauthToken getOauthToken(OauthCallback callback) {
        try {
            return tryToGetOauthToken(callback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OauthToken tryToGetOauthToken(OauthCallback callback) throws IOException {
        String requestURI = buildRequestURI(callback);
        URL url = new URL(requestURI);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        String json = readResponseBody(conn, responseCode);
        NaverToken token = parseNaverToken(json);

        return token;
    }

    private String buildRequestURI(OauthCallback callback) throws UnsupportedEncodingException {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(naverOauth.getBaseUrl() + "/token")
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", naverOauth.getClientId())
                .queryParam("client_secret", naverOauth.getClientSecret())
                .queryParam("code", callback.getCode())
                .queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
                .build();
        return uriComponents.toString();
    }

    private String readResponseBody(HttpURLConnection conn, int responseCode) throws IOException {
        InputStream inputStream = responseCode == 200 ?  conn.getInputStream() : conn.getErrorStream();
        // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            StringBuilder responseBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                responseBody.append(line);
            }
            return responseBody.toString();
        }
    }

    private NaverToken parseNaverToken(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, NaverToken.class);
    }

    @Override
    public OauthUserInfo getUserInfoByToken(OauthToken token) {
        try {
           return tryToGetUserInfoByToken(token);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }

    private OauthUserInfo tryToGetUserInfoByToken(OauthToken token) throws IOException {
        String accessToken = token.getAccess_token();
        String tokenType = token.getToken_type();

        URL url = new URL(NAVER_USER_INFO_REQ_URL);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", tokenType + " " + accessToken);

        int responseCode = conn.getResponseCode();
        String json = readResponseBody(conn, responseCode);
        return parseUserInfo(json);
    }

    private OauthUserInfo parseUserInfo(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        NaverApiResponse apiResponse =  objectMapper.readValue(json, NaverApiResponse.class);
        return OauthUserInfo.fromNaverApiResponse(apiResponse);
    }

    @Override
    public void unlink(String accessToken) {

    }
}
