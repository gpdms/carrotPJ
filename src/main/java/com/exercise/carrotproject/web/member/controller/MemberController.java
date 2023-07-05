package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.BuyPostDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.repository.TradeCustomRepositoryImpl;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.member.error.ErrorCode;
import com.exercise.carrotproject.web.member.error.ErrorResponse;
import com.exercise.carrotproject.web.member.form.*;
import com.exercise.carrotproject.web.member.form.memberInfo.ProfileForm;
import com.exercise.carrotproject.web.member.form.memberInfo.PwdUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final TradeCustomRepositoryImpl tradeCustomRepository;

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("isSocial", false);
        return "member/signupForm";
    }

    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signup(@Valid @RequestBody final SignupForm form) {
        if (memberService.hasDuplicatedMemId(form.getMemId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_MEM_ID));
        }
        if (memberService.hasDuplicatedMemId(form.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        Member member = Member.builder().memId(form.getMemId())
                .memPwd(form.getPwd())
                .email(form.getEmail())
                .nickname(form.getNickname())
                .loc(form.getLoc())
                .role(Role.USER).build();
        memberService.insertMember(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signup/memId/{memId}")
    @ResponseBody
    public ResponseEntity memIdDuplicateCheck(@PathVariable(required = true) String memId) {
        if(memberService.hasDuplicatedMemId(memId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_MEM_ID));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signup/email/{email}")
    @ResponseBody
    public ResponseEntity emailDuplicateCheck(@PathVariable(required = true) String email) {
        if(memberService.hasDuplicatedEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("signup/email/auth-code")
    @ResponseBody
    public ResponseEntity sendAuthCodeByEmail(@Valid @RequestBody EmailRequestForm form) {
        if(memberService.hasDuplicatedEmail(form.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        String authCode = memberService.sendAuthCodeByEmail(form.getEmail());
        return ResponseEntity.ok().body(authCode);
    }

    @GetMapping("pwd/find")
    public String toFindForm (){
        return "member/findPwd";
    }

    @PostMapping("pwd/find")
    @ResponseBody
    public ResponseEntity sendTemporaryPwdByEmail (@Valid EmailRequestForm emailRequestForm,
                                                    BindingResult bindingResult) {
        Map<String, String> resultMap = new HashMap<>();
        if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("email").getDefaultMessage();
            resultMap.put("email", errorMessage);
            return ResponseEntity.badRequest().body(resultMap);
        }
        boolean hasEmail = memberService.hasDuplicatedEmail(emailRequestForm.getEmail());
        if (!hasEmail) {
            resultMap.put("no-email", "가입하지 않은 이메일입니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        memberService.issueTemporaryPwdByEmail(emailRequestForm.getEmail());
        resultMap.put("success", "임시 비밀번호 발급");
        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("/settings/pwd")
    public String pwdUpdateForm() {
        return "myPage/pwdUpdate";
    }

    @PatchMapping("/{memId}/pwd")
    @ResponseBody
    public ResponseEntity pwdUpdate(@PathVariable String memId,
                            @Valid @RequestBody final PwdUpdateForm form) {
        boolean isSamePwdAndPwdConfirm = form.getPwd().equals(form.getPwdConfirm());
        if (!isSamePwdAndPwdConfirm) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.NOT_CORRECT_PWD_CONFIRM));
        }
        memberService.changePwdByMemId(form.getPwd(), memId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/profile")
    public String profileEditForm(@Login MemberDto loginMember,
                                  @ModelAttribute("profileForm") ProfileForm form) {
        form.setMemId(loginMember.getMemId());
        form.setNickname(loginMember.getNickname());
        form.setLoc(loginMember.getLoc());
        return "myPage/profileUpdate";
    }

    @ResponseBody
    @PatchMapping("/{memId}/profile")
    public ResponseEntity profileUpdate(@Login MemberDto loginMember,
                                        @Valid ProfileForm profileForm,
                                        @RequestParam("profImg") MultipartFile profImg) {
        Map<String, Object> resultMap = new HashMap<>();
        Member updateMember = Member.builder().memId(profileForm.getMemId())
                .nickname(profileForm.getNickname())
                .loc(profileForm.getLoc()).build();

        resultMap = memberService.changeProfile(updateMember, profImg);
        if(!resultMap.containsKey("success")){
            return ResponseEntity.badRequest().body(resultMap);
        } else {
            Member after = (Member) resultMap.get("success");
            loginMember.setNickname(after.getNickname());
            loginMember.setLoc(after.getLoc());
            Member member = (Member) resultMap.get("success");
            resultMap.clear();
            resultMap.put("member", new ProfileForm(member.getMemId(), member.getProfPath(), member.getNickname(), member.getLoc()));
            return ResponseEntity.ok().body(resultMap);
        }
    }

    @GetMapping("/{memId}/trade/buy-list")
    public String myBuyList(@PathVariable String memId, Model model) {
        List<BuyPostDto> myBuyList = tradeCustomRepository.findBuyListByMemId(memId);
        model.addAttribute("buyList", myBuyList);
        return "myPage/buyList";
    }

    @GetMapping("/{memId}/trade/sell-list")
    public String mySellList(@PathVariable String memId, Model model){
        //판매중,예약중 게시글
        Map map = postService.selectPostBySellState(memId);
        List<PostDto> onSaleAndRsvList = (List) map.get("onSaleAndRsvList");
        //판매완료
        List<SoldPostDto> soldList = (List) map.get("soldList");

        model.addAttribute("onSaleAndRsv", onSaleAndRsvList);
        model.addAttribute("soldList", soldList);

        //숨김 게시글
        List<PostDto> hidePostList = postService.selectHidePost(memId);
        model.addAttribute("hidePostList", hidePostList);
        return "myPage/sellList";
    }


    @GetMapping("/{memId}/wish-list")
    public String wishList(@PathVariable String memId, Model model){
        List<PostDto> postList = postService.selectPostListFromWish(memId);
        model.addAttribute("postList", postList);
        return "myPage/wishList";
    }
}
