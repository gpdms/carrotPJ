package com.exercise.carrotproject.web.member;


import com.exercise.carrotproject.domain.member.MemberService;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService  memberService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("memberSaveForm") MemberSaveForm form) {
        return "/member/signupForm";
    }
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("memberSaveForm") MemberSaveForm form,
                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {;
            return  "/member/signupForm";
        }
        if (!form.getPwd().equals(form.getPwdConfirm())) {
            bindingResult.rejectValue("pwdConfirm", "pwdConfirmInCorrect");
            return "/member/signupForm";
        }

        Member member = Member.builder().memId(form.getMemId())
                .memPwd(form.getPwd())
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        Map<String, Object> saveResult = memberService.saveMember(member);
        if (saveResult.containsValue("DuplicatedMember")) {
            bindingResult.rejectValue("memId", "duplicatedMemId");
            return "/member/signupForm";
        }

        return "redirect:/";
    }
}
