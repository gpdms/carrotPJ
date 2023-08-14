package com.exercise.carrotproject.domain.member.ouath.kakao;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.ouath.OauthCallback;
import com.exercise.carrotproject.domain.member.ouath.OauthService;
import com.exercise.carrotproject.domain.member.ouath.OauthToken;
import com.exercise.carrotproject.domain.member.ouath.OauthUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class KakaoOauthService implements OauthService {
    private final KaKaoOauth kaKaoOauth;
    private static final String KAKAO_USER_INFO_REQ_URL = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_UNLINK_REQ_URL =  "https://kapi.kakao.com/v1/user/unlink";

    @Override
    public boolean supports(Role role) {
        return role == Role.SOCIAL_KAKAO;
    }

    @Override
    public String getAuthorizeUrl() {
        return kaKaoOauth.getBaseUrl()
                + "/authorize?client_id=" + kaKaoOauth.getClientId()
                + "&redirect_uri=" + kaKaoOauth.getLoginRedirectUri()
                + "&response_type=code";
    }

    @Override
    public OauthToken getOauthToken(OauthCallback callback){
        try {
            return tryToGetOauthToken(callback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OauthToken tryToGetOauthToken(OauthCallback callback) throws IOException {
        URL url = new URL(kaKaoOauth.getBaseUrl()+"/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true); // POST 요청을 위해 기본값이 false인 setDoOutput을 true로

        sendAuthCode(conn, callback.getCode());
        int responseCode = conn.getResponseCode();
        String json = readResponseBody(conn, responseCode);
        KakaoToken kakaoToken = parseKaKaoToken(json);

        return kakaoToken;
    }

    private void sendAuthCode(HttpURLConnection conn, String authCode) throws IOException {
        // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
        try(BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()))) {
            String body = buildRequestBody(authCode);
            bw.write(body);
            bw.flush();
        }
    }

    private String buildRequestBody(String authCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");
        sb.append("&client_id="+kaKaoOauth.getClientId());
        sb.append("&redirect_uri="+kaKaoOauth.getLoginRedirectUri());
        sb.append("&code=" + authCode);
        return sb.toString();
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

    private KakaoToken parseKaKaoToken(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, KakaoToken.class);
    }

    @Override
    public OauthUserInfo getUserInfoByToken(OauthToken oauthToken)  {
        try {
            return tryToGetUserInfo(oauthToken.getAccess_token());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OauthUserInfo tryToGetUserInfo(String accessToken) throws IOException {
        URL url = new URL(KAKAO_USER_INFO_REQ_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken); // 요청에 필요한 Header에 포함될 내용

        int responseCode = conn.getResponseCode();
        String json = readResponseBody(conn,responseCode);
        return parseUserInfo(json);
    }

    private OauthUserInfo parseUserInfo(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        KakaoApiResponse apiResponse = objectMapper.readValue(json, KakaoApiResponse.class);
        return OauthUserInfo.fromKakaoApiResponse(apiResponse);
    }

    @Override
    public void unlink(String accessToken) {
        try {
            tryToUnlink(accessToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tryToUnlink(String accessToken) throws IOException {
        URL unlinkUrl = new URL(KAKAO_UNLINK_REQ_URL);
        HttpURLConnection conn = (HttpURLConnection) unlinkUrl.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        int responseCode = conn.getResponseCode();
    }
}
