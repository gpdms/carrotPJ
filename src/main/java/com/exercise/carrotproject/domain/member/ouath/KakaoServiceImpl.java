package com.exercise.carrotproject.domain.member.ouath;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl {
    private final KaKaoOauth kaKaoOauth;
    private final MemberRepository memberRepository;

    public String getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String reqURL  = "https://kauth.kakao.com/oauth/token";
        String accessToken = "";
        String refreshToken = "";

        URL url = new URL(reqURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            // POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id="+kaKaoOauth.getKakaoClientId());
            sb.append("&redirect_uri="+kaKaoOauth.getKakaoLoginRedirectUri());
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();
            // 결과 코드가 200이라면 성공
            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });

            accessToken = jsonMap.get("access_token").toString();
            refreshToken = jsonMap.get("refresh_token").toString();

            System.out.println("accessToken = " + accessToken);
            System.out.println("refreshToken = " + refreshToken);


            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) throws Throwable {
        // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
        HashMap<String, Object> userInfo = new HashMap<String, Object>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 요청에 필요한 Header에 포함될 내용
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);
            System.out.println("result type" + result.getClass().getName()); // java.lang.String

            try {
                // jackson objectmapper 객체 생성
                ObjectMapper objectMapper = new ObjectMapper();
                // JSON String -> Map
                Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>(){});

                Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");
                Map<String, Object> profile = (Map<String, Object>) kakao_account.get("profile");

                String nickname = profile.get("nickname").toString();
                String email = kakao_account.get("email").toString();
                String profileImgUrl = "";
                if(!(Boolean)profile.get("is_default_image")) {
                    profileImgUrl = profile.get("profile_img_url").toString();
                }
                userInfo.put("nickname", nickname);
                userInfo.put("email", email);
                userInfo.put("profileImgUrl", profileImgUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

/*    public void isExsitedKakaoMember(String email, Role role) {
        memberRepository.existsByEmailAndRole()
        boolean existsByEmailAndRole(String email, Role role);
    }*/

  /*  public void kakaoLogout(String accessToken) {
        String reqURL = "https://kapi.kakao.com/v1/user/logout";
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String result = "";
            String line = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
