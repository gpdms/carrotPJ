package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.ouath.KaKaoOauth;
import com.exercise.carrotproject.domain.member.ouath.KakaoServiceImpl;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.SignupForm;
import com.exercise.carrotproject.web.member.form.SignupSocialForm;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OauthController {
    private final KaKaoOauth kaKaoOauth;
    private final KakaoServiceImpl kakaoService;
    private final MemberService memberService;

    @GetMapping("/login/getKakaoLoginURL")
    public String toKakaoLoginForm() {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/authorize?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&redirect_uri=" + kaKaoOauth.getKakaoLoginRedirectUri() + "&response_type=code";
        return "redirect:"+kakaoUrl;
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model, HttpServletRequest request) throws Throwable {
        HttpSession session = request.getSession();
        //해당 페이지에서 새로고침시 : 인증 code가 파기되어 오류페이지가 뜨지 않도록, login페이지로 이동시킨다.
        String revokedCode = (String) session.getAttribute(SessionConst.KAKAO_REVOKED_CODE);
        if (revokedCode!= null && revokedCode.equals(code)) {
            session.removeAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
            return "redirect:/login";
        }
        //새로고침이 아니라 해당 url로 처음 요청하는 경우
        session.setAttribute(SessionConst.KAKAO_REVOKED_CODE, code);
        String accessToken = kakaoService.getAccessToken(code);
        HashMap<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        log.info("userInfo 프로필 이미지 = {} ", userInfo.get("profPath").toString());
        log.info("userInfo 닉네임 = {} ", userInfo.get("nickname").toString());
        log.info("userInfo 이메일 = {} ", userInfo.get("email").toString());
        String email = userInfo.get("email").toString();

        boolean hasKakaoMember = memberService.hasEmailAndRole(email, Role.SOCIAL_KAKAO);
        if(!hasKakaoMember) {
            session.setAttribute(SessionConst.KAKAO_ACCESS_TOKEN, accessToken);
            model.addAttribute("userInfo",userInfo);
            model.addAttribute("platform", "kakao");
            model.addAttribute("isSocial", true);
            return "member/signupForm";
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberService.findOneSocialMemberDto(email, Role.SOCIAL_KAKAO));
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return "redirect:/";
    }

    @GetMapping("/unlink/kakao")
    @ResponseBody
    public String unlink(HttpSession session) throws Throwable {
        String accessToken = (String) session.getAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
        kakaoService.kakaoUnlink(accessToken);
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return "성공";
    }

    @PostMapping("/signup/kakao")
    @ResponseBody
    public ResponseEntity<?> kakaoSignup(@Valid @RequestBody SignupSocialForm signupSocialForm,
                                      BindingResult result,
                                      HttpSession session,
                                      Model model) throws Throwable {
        HashMap<String, Object> errorMap = new HashMap<>();
        if(result.hasErrors()) {
            errorMap.put("nickname", result.getFieldError("nickname").getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }
        HashMap<String, Object> userInfo = new HashMap<>();
        userInfo.put("loc", signupSocialForm.getLoc());
        userInfo.put("profPath", signupSocialForm.getProfPath());
        userInfo.put("nickname", signupSocialForm.getNickname());
        userInfo.put("email", signupSocialForm.getEmail());
        Map<String, Object> saveResult= memberService.insertSocialMember(userInfo, Role.SOCIAL_KAKAO);
        if (saveResult.containsKey("fail")) {
            errorMap.put("nickname", "중복된 닉네임입니다.");
            return ResponseEntity.badRequest().body(errorMap);
        }
        session.setAttribute(SessionConst.LOGIN_MEMBER, saveResult.get("memberDto"));
        session.removeAttribute(SessionConst.KAKAO_REVOKED_CODE);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/logout/getKakaoLogoutURL")
    @ResponseBody
    public String toKakaoLogoutForm(HttpSession session) {
        String kakaoUrl = kaKaoOauth.getKakaoLoginUrl()
                + "/oauth/logout?client_id=" + kaKaoOauth.getKakaoClientId()
                + "&logout_redirect_uri=" + kaKaoOauth.getKakaoLogoutRedirectUri();
        return kakaoUrl;
    }
}





