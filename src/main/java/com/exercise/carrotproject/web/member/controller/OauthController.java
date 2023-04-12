package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.ouath.KaKaoOauth;
import com.exercise.carrotproject.domain.member.ouath.KakaoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OauthController {
    private final KaKaoOauth kaKaoOauth;
    private final KakaoServiceImpl kakaoService;

    @GetMapping("/login/getKakaoLoginURL")
    @ResponseBody
    public String toKakaoLoginForm(){
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                +"/oauth/authorize?client_id="+kaKaoOauth.getKakaoClientId()
                +"&redirect_uri="+kaKaoOauth.getKakaoLoginRedirectUri()+"&response_type=code";
        return kakaoUrl;
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model) throws Throwable {
        System.out.println("code = " + code);
        //access토큰얻기
        String accessToken = kakaoService.getToken(code);
        System.out.println("###access_Token#### : " + accessToken);
        //토큰으로 유저정보얻기
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        System.out.println("userInfo 프로필 이미지 = " + userInfo.get("profileImgUrl"));
        System.out.println("userInfo 닉네임 = " + userInfo.get("nickname"));
        System.out.println("userInfo 프로필 이미지 = " + userInfo.get("email"));


        //ci는 비즈니스 전환후 검수신청 -> 허락받아야 수집 가능
        return "redirect:/";
    }

    @GetMapping("/logout/getKakaoLogoutURL")
    @ResponseBody
    public String toKakaoLogoutForm() {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                +"/oauth/logout?client_id="+kaKaoOauth.getKakaoClientId()
                +"&logout_redirect_uri="+kaKaoOauth.getKakaoLoginRedirectUri();
        return kakaoUrl;
    }

/*    @GetMapping("/logout/kakao")
    @ResponseBody
    public String kakaoLogout() {

    }*/


