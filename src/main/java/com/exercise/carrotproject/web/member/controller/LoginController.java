package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.ouath.GoogleOauth;
import com.exercise.carrotproject.domain.member.service.OauthServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.service.LoginService;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.web.member.form.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;
    private final GoogleOauth googleOauth;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm form,
                          BindingResult bindingResult,
                          @RequestParam(defaultValue = "/") String redirectURL,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "member/loginForm";
        }
        Member loginMember = loginService.login(form.getLoginId(), form.getPwd());
        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "member/loginForm";
        }
        MemberDto loginMemberDto = MemberDto.builder().memId(loginMember.getMemId())
                .nickname(loginMember.getNickname())
                .mannerScore(loginMember.getMannerScore())
                .loc(loginMember.getLoc()).build();

        //로그인 성공 처리
        //세션이 있으면 있는 세션 반환, 없으면 신규 세션을 생성
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보 보관
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberDto);
        //로그인 성공시 이전 페이지(요청온 페이지)로 이동
        return "redirect:" + redirectURL;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    // 구글 로그인창 호출
/*    @GetMapping ("/login/getGoogleAuthUrl")
    @ResponseBody
    public String getGoogleAuthUrl(HttpServletRequest request) throws Exception {
        String reqUrl = googleOauth.getGoogleLoginUrl() +
                "/o/oauth2/v2/auth?client_id="
                + googleOauth.getGoogleClientId() +
                "&redirect_uri=" + googleOauth.getGoogleRedirectUrl()
                + "&response_type=code&scope=email%20profile%20openid&access_type=offline";
        return reqUrl;
    }*/
}
