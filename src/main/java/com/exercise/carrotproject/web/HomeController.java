package com.exercise.carrotproject.web;


import com.exercise.carrotproject.SessionConst;
import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MemberRepository memberRepository;

    @GetMapping("/init")
    public String init(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member loginMember = memberRepository.findById("jk65333").orElseThrow();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(@Login Member loginMember, Model model) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인된 홈으로 이동
        model.addAttribute("member", loginMember);
        return "userHome";
    }

}
