package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.JoinSocialMemberRequest;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.ouath.*;
import com.exercise.carrotproject.domain.member.ouath.kakao.KaKaoOauth;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.web.argumentresolver.LoginType;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.JoinSocialForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OauthController {
    private final KaKaoOauth kaKaoOauth;
    private final OauthServiceFactory oauthServiceFactory;
    private final MemberService memberService;

    @GetMapping("/oauth")
    public String toOauthLoginForm(@LoginType final Role role) {
        OauthService oauthService = oauthServiceFactory.find(role);
        String authorizeUrl = oauthService.getAuthorizeUrl();
        return "redirect:"+ authorizeUrl;
    }

    @GetMapping("/login-social")
    public String socialLogin(@LoginType final Role role,
                              @ModelAttribute final OauthCallback callback,
                              HttpSession session, Model model) {
        if (callback.getError() != null) {
            return "redirect:/login";
        }

        //해당 페이지에서 새로고침시 : 인증 code가 파기되어 오류페이지가 뜨지 않도록, login페이지로 이동
        String revokedCode = (String) session.getAttribute(SessionConst.REVOKED_CODE);
        if (revokedCode != null && revokedCode.equals(callback.getCode())) {
            session.removeAttribute(SessionConst.ACCESS_TOKEN);
            return "redirect:/login";
        }

        //새로고침이 아니라 해당 url로 처음(정상적으로) 진입하는 경우
        session.setAttribute(SessionConst.REVOKED_CODE, callback.getCode());

        OauthService oauthService = oauthServiceFactory.find(role);
        OauthToken token = oauthService.getOauthToken(callback);
        OauthUserInfo userInfo = oauthService.getUserInfoByToken(token);

        String email = userInfo.getEmail();
        boolean hasMember = memberService.hasEmail(email);
        if(!hasMember) {
            session.setAttribute(SessionConst.ACCESS_TOKEN, token.getAccess_token());
            String platform = role == Role.SOCIAL_KAKAO ? "kakao" : "naver";
            model.addAttribute("userInfo", userInfo);
            model.addAttribute("platform", platform);
            model.addAttribute("isSocial", true);
            return "member/joinForm";
        }

        Role memberRole = memberService.findMemberByEmail(email).getRole();
        if (role == memberRole) {
            MemberDto memberDto = memberService.login(email, role);
            session.setAttribute(SessionConst.LOGIN_MEMBER, memberDto);
        } else { //같은 이메일이 존재하지만, 다른 타입으로 가입한 경우
            model.addAttribute("joinedRole", memberRole);
            return "member/loginForm";
        }
        session.removeAttribute(SessionConst.REVOKED_CODE);
        return "redirect:/";
    }

    @PostMapping("members/join-social")
    @ResponseBody
    public ResponseEntity socialJoin(@Valid @RequestBody final JoinSocialForm joinSocialForm,
                                     @LoginType final Role role,
                                     BindingResult result,
                                     HttpSession session){
        if(result.hasErrors()) {
            Map<String, Object> errorMap = new HashMap<>();
            errorMap.put("nickname", result.getFieldError("nickname").getDefaultMessage());
            return ResponseEntity.badRequest().body(errorMap);
        }

        JoinSocialMemberRequest member = JoinSocialMemberRequest.builder()
                .email(joinSocialForm.getEmail())
                .profImgUrl(joinSocialForm.getProfImgUrl())
                .loc(joinSocialForm.getLoc())
                .nickname(joinSocialForm.getNickname())
                .role(role)
                .build();
        memberService.joinSocialMember(member);

        MemberDto memberDto = memberService.login(member.getEmail(), role);
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberDto);
        session.removeAttribute(SessionConst.REVOKED_CODE);
        return ResponseEntity.ok().build();
    }

    @GetMapping("logout-social")
    @ResponseBody
    public String socialLogout(@RequestParam final String platform) {
        if (platform.equals("kakao")) {
            String kakaoUrl = kaKaoOauth.getBaseUrl()
                    + "/oauth/logout?client_id=" + kaKaoOauth.getClientId()
                    + "&logout_redirect_uri=" + kaKaoOauth.getLogoutRedirectUri();
            return kakaoUrl;
        }
        return "/logout";
    }

/*    @GetMapping("/unlink/kakao")
    @ResponseBody
    public ResponseEntity unlink(HttpSession session) throws Throwable {
        String accessToken = (String) session.getAttribute(SessionConst.ACCESS_TOKEN);
        OauthService oauthService = oauthServiceFactory.find(Role.SOCIAL_KAKAO);
        oauthService.unlink(accessToken);
        session.removeAttribute(SessionConst.REVOKED_CODE);
        return ResponseEntity.ok().build();
    }*/
}





