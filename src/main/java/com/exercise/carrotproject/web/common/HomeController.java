package com.exercise.carrotproject.web.common;


import com.exercise.carrotproject.domain.member.MemberService;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public String home(@Login Member loginMember, Model model) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인된 홈으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

}
