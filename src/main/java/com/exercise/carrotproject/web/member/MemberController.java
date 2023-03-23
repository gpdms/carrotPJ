package com.exercise.carrotproject.web.member;


import com.exercise.carrotproject.domain.member.MemberService;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import com.exercise.carrotproject.web.member.form.ProfileForm;
import com.exercise.carrotproject.web.member.form.PwdUpdateForm;
import com.exercise.carrotproject.web.member.form.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService  memberService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("signupForm") SignupForm form) {
        return "/member/signupForm";
    }
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupForm") SignupForm form,
                    BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {;
            return  "/member/signupForm";
        }
        if (!form.getPwd().equals(form.getPwdConfirm())) {
            bindingResult.rejectValue("pwdConfirm", "pwdConfirmInCorrect", "암호가 일치하지 않습니다.");
            return "/member/signupForm";
        }

        Member member = Member.builder().memId(form.getMemId())
                .memPwd(form.getPwd())
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        Map<String, Object> saveResult = memberService.saveMember(member);
        //log.info("saveResult {}", saveResult.containsValue("fail-DM"));
        if (saveResult.containsValue("fail-DM")) {
            bindingResult.rejectValue("memId", "duplicatedMemId", "이미 존재하는 아이디입니다.");
            return "/member/signupForm";
        }

        return "redirect:/";
    }

    @GetMapping("/{memId}")
    public String memberInfoForm(@PathVariable String memId,
                                 @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm,
                                 @ModelAttribute("profileForm")ProfileForm profileForm) {
        MemberDto member = memberService.findMemberForProfileEdit(memId);
        profileForm.setProfPath(member.getProfPath());
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());
        return "/member/memberInfo";
    }
    @PostMapping("/{memId}")
    public String memberPwdEdit(@PathVariable String memId,
                             @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm,
                             @ModelAttribute("profileForm")ProfileForm profileForm) {
        MemberDto member = memberService.findMemberForProfileEdit(memId);
        profileForm.setProfPath(member.getProfPath());
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());


        return "/member/memberInfo";
    }

    @PatchMapping("/{memId}")
    public String memberProfEdit(@PathVariable String memId,
                                 @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm,
                                 @ModelAttribute("profileForm")ProfileForm profileForm) {
        MemberDto member = memberService.findMemberForProfileEdit(memId);
        profileForm.setProfPath(member.getProfPath());
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());
        return "/member/memberInfo";
    }

}
