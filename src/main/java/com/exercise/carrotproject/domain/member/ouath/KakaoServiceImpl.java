package com.exercise.carrotproject.domain.member.ouath;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KakaoServiceImpl {
    private final KaKaoOauth kaKaoOauth;
    private final MemberRepository memberRepository;

    // 인가코드로 토큰받기
    public String getAccessToken(String code) throws IOException {
        String reqURL  = "https://kauth.kakao.com/oauth/token";
        String accessToken = "";
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

            // 요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            String result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("getToken_response body : " + result);

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>() {
            });
            accessToken = jsonMap.get("access_token").toString();
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public HashMap<String, Object> getUserInfo(String accessToken) throws Throwable {
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

            // jackson objectmapper 객체 생성
            ObjectMapper objectMapper = new ObjectMapper();
            // JSON String -> Map
            Map<String, Object> jsonMap = objectMapper.readValue(result, new TypeReference<Map<String, Object>>(){});
            Map<String, Object> kakao_account = (Map<String, Object>) jsonMap.get("kakao_account");
            Map<String, Object> profile = new HashMap<>();
            if (kakao_account.containsKey("profile")) {
                profile = (Map<String, Object>) kakao_account.get("profile");
            };
            //선택동의 항목처리
            String profileImgUrl = "";
            String nickname = "";
            if (!profile.isEmpty()) {
                if(profile.containsKey("is_default_image") && !(boolean)profile.get("is_default_image")) {
                    profileImgUrl = profile.get("profile_image_url").toString();  //디폴트 이미지면 빈문자열만 보낸다.
                }
                if(profile.containsKey("nickname")) {
                    nickname = profile.get("nickname").toString();
                }
            }
            userInfo.put("nickname", nickname);
            userInfo.put("email", kakao_account.get("email"));
            userInfo.put("profPath", profileImgUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public void kakaoUnlink(String accessToken) {
        String unlinkReqUrl = "https://kapi.kakao.com/v1/user/unlink";
        try {
            URL unlinkUrl = new URL(unlinkReqUrl);
            HttpURLConnection conn = (HttpURLConnection) unlinkUrl.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode Unlink : " + responseCode);
        }  catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   /* public String getAgreementInfo(String access_token) {
        String result = "";
        String host = "https://kapi.kakao.com/v2/user/scopes";
        try{
            URL url = new URL(host);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Authorization", "Bearer "+access_token);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            while((line=br.readLine())!=null) {
                result+=line;
            }
            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);
            // result is json format
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }*/


}
