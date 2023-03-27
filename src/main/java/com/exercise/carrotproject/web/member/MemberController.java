package com.exercise.carrotproject.web.member;


import com.exercise.carrotproject.SessionConst;
import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.MemberService;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.entity.MemberDto;
import com.exercise.carrotproject.web.member.form.ProfileForm;
import com.exercise.carrotproject.web.member.form.PwdUpdateForm;
import com.exercise.carrotproject.web.member.form.SignupForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @GetMapping("/{memId}")
    public String toUserHome(@PathVariable String memId, Model model){
        Member member = memberRepository.findById(memId).orElseThrow();
        model.addAttribute("member", member);
        return "/userHome";
    }

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
            bindingResult.rejectValue("pwdConfirm", "pwdConfirmIncorrect");
            return "/member/signupForm";
        }

        Member member = Member.builder().memId(form.getMemId())
                .memPwd(form.getPwd())
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        Map<String, Object> saveResult = memberService.saveMember(member);
        //log.info("saveResult {}", saveResult.containsValue("fail-DM"));
        //중복된 아이디 -> field 오류로 알릴 수 있다.
        if (saveResult.containsValue("fail-DM")) {
            bindingResult.rejectValue("memId", "duplicatedMemId");
            return "/member/signupForm";
        }

        return "redirect:/";
    }

    @GetMapping("/{memId}/edit")
    public String memberInfoForm(@PathVariable String memId,
                                 @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm,
                                 @ModelAttribute("profileForm") ProfileForm profileForm) {
        Member member = memberService.findMemberForProfileEdit(memId);
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());
        return "/member/memberInfo";
    }

    @PostMapping("/{memId}/pwdEdit")
    public String pwdUpdate(@PathVariable String memId,
                            @ModelAttribute("profileForm") ProfileForm profileForm,
                            @Valid @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm,
                            BindingResult bindingResult) {
        //BindingResult나 Errors는 바인딩 받는 객체 바로 다음에 선언해야 한다
        Member member = memberService.findMemberForProfileEdit(memId);
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());
        if(bindingResult.hasErrors()) {;
            return  "/member/memberInfo";
        }
        if (!pwdUpdateForm.getPwd().equals(pwdUpdateForm.getPwdConfirm())) {
            bindingResult.rejectValue("pwdConfirm", "pwdConfirmIncorrect", "암호가 일치하지 않습니다.");
            return  "/member/memberInfo";
        }

        //db에 업데이트 실패 -> 검증오류(PwdUpdateForm에 관한 문제) 아니고, 서버 오류일 것이다
        //bindingResult에 담아서 보내지 말아야하지 않을까?
        boolean isPwdUpdated = memberService.isPwdUpdated(memId, pwdUpdateForm.getPwd());
        if (!isPwdUpdated) {
            return  "/member/memberInfo";
        }
        return  "/member/memberInfo";
    }

    @ResponseBody
    @PatchMapping("/{memId}/profileEdit")
    public ProfileForm profileUpdate(@PathVariable String memId,
                                @Valid @ModelAttribute("profileForm") ProfileForm profileForm,
                                @RequestParam("profImg") MultipartFile profImg) {
        Member updateMember = Member.builder().memId(profileForm.getMemId())
                .nickname(profileForm.getNickname())
                .loc(profileForm.getLoc()).build();
        Member member = memberService.profileUpdate(updateMember, profImg);
        return new ProfileForm(member.getMemId(), member.getProfPath(), member.getNickname(),member.getLoc());
    }

    //프로필 이미지 출력
    @ResponseBody
    @GetMapping("/{memId}/profileImg")
    public Resource viewProfileImg(@PathVariable("memId") String memId) throws IOException {
        String profPath = memberService.getProfPath(memId);
        UrlResource urlResource = new UrlResource("file:" + profPath);
        return urlResource;
    }

}
