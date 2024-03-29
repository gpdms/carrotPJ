package com.exercise.carrotproject.web.member.controller;

import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.web.member.form.LoginForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "member/loginForm";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") final LoginForm form,
                        BindingResult bindingResult,
                        @RequestParam(defaultValue = "/") final String redirectURL,
                        HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 다시 확인해주세요.");
            return "member/loginForm";
        }

        MemberDto member = memberService.login(form.getLoginId(), form.getPwd());
        if (member == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호를 다시 확인해주세요.");
            return "member/loginForm";
        }

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
