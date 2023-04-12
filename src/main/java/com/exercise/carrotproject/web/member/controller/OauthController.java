package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.ouath.KaKaoOauth;
import com.exercise.carrotproject.domain.member.ouath.KakaoServiceImpl;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OauthController {
    private final KaKaoOauth kaKaoOauth;
    private final KakaoServiceImpl kakaoService;
    private final MemberServiceImpl memberService;

    @GetMapping("/login/getKakaoLoginURL")
    @ResponseBody
    public String toKakaoLoginForm() {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/authorize?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&redirect_uri=" + kaKaoOauth.getKakaoLoginRedirectUri() + "&response_type=code";
        return kakaoUrl;
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model, HttpSession session) throws Throwable {
        System.out.println("code = " + code);
        //access토큰얻기
        String accessToken = kakaoService.getToken(code);
        System.out.println("###access_Token#### : " + accessToken);
        session.setAttribute(SessionConst.KAKAO_ACCESS_TOKEN, accessToken);
        //토큰으로 유저정보얻기
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        System.out.println("userInfo 프로필 이미지 = " + userInfo.get("profileImgUrl").toString());
        System.out.println("userInfo 닉네임 = " + userInfo.get("nickname").toString());
        System.out.println("userInfo 프로필 이미지 = " + userInfo.get("email").toString());
        String email = userInfo.get("email").toString();
        boolean hasKakaoMember = memberService.hasSocialMember(email, Role.SOCIAL_KAKAO);
        if(!hasKakaoMember) {
            model.addAttribute("platform", "kakao");
            model.addAttribute("isSocial", true);
            return "/member/signupForm";
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberService.findOneSocialMember(email, Role.SOCIAL_KAKAO));
        return "redirect:/";
    }

    @PostMapping("/signup/kakao")
    @ResponseBody
    public String kakaoSingUp(@RequestBody HashMap<String, Loc> map, HttpSession session) throws Throwable {
        String accessToken = (String) session.getAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        userInfo.put("loc", map.get("loc"));
        MemberDto memberDto = memberService.insertSocialMember(userInfo, Role.SOCIAL_KAKAO);
        session.removeAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberDto);
        return "성공";
    }

    @GetMapping("/logout/getKakaoLogoutURL")
    @ResponseBody
    public String toKakaoLogoutForm() {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/logout?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&logout_redirect_uri=" + kaKaoOauth.getKakaoLoginRedirectUri();
        return kakaoUrl;
    }

/*    @GetMapping("/logout/kakao")
    @ResponseBody
    public String kakaoLogout() {

    }*/
}


