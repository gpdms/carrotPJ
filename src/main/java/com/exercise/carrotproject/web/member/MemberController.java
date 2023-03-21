package com.exercise.carrotproject.web.member;


import com.exercise.carrotproject.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;

    @GetMapping("/join")
    public String joinForm(@ModelAttribute("memberJoinForm") MemberJoinForm memberJoinForm) {
        return "members/memberJoinForm";
    }
/*    @PostMapping("/join")
    public String save(@Valid @ModelAttribute MemberJoinForm memberJoinForm, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "members/memberJoinForm";
        }
        memberRepository.save(member);
        return "redirect:/";
    }*/
}
