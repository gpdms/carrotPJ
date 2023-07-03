package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.EmailServiceImpl;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.TradeCustomRepositoryImpl;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewBuyerService;
import com.exercise.carrotproject.domain.review.service.ReviewSellerService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.error.ErrorCode;
import com.exercise.carrotproject.web.member.form.*;
import com.exercise.carrotproject.web.member.form.memberInfo.ProfileForm;
import com.exercise.carrotproject.web.member.form.memberInfo.PwdUpdateForm;
import com.exercise.carrotproject.domain.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
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
        return "/member/signupForm";
    }
    @PostMapping("/signup")
    @ResponseBody
    public ResponseEntity signup(@Valid @RequestBody final SignupForm form) {
        boolean isSamePwdAndPwdConfirm = form.getPwd().equals(form.getPwdConfirm());
        if (!isSamePwdAndPwdConfirm) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다");
        }
        if (memberService.hasDuplicatedMemId(form.getMemId())) {
            return ResponseEntity.badRequest().body(ErrorCode.DUPLICATED_MEM_ID);
        }
        if (memberService.hasDuplicatedEmail(form.getEmail())) {
            return ResponseEntity.badRequest().body(ErrorCode.DUPLICATED_EMAIL);
        }
        String hashedPwd = SecurityUtils.getHashedPwd(form.getPwd());
        Member member = Member.builder().memId(form.getMemId())
                .memPwd(hashedPwd)
                .nickname(form.getNickname())
                .loc(form.getLoc())
                .role(Role.USER).build();
        memberService.insertMember(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/signup/memId/{memId}")
    @ResponseBody
    public ResponseEntity memIdDuplicateCheck(@PathVariable String memId) {
        if(memberService.hasDuplicatedMemId(memId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorCode.DUPLICATED_MEM_ID);
        }
        return ResponseEntity.ok().build();
    }
    @GetMapping("/signup/email/{email}")
    @ResponseBody
    public ResponseEntity emailDuplicateCheck(@PathVariable String email) {
        if(memberService.hasDuplicatedMemId(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorCode.DUPLICATED_EMAIL);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("signup/email/verify")
    @ResponseBody
    public ResponseEntity verifyEmail(@Valid @ModelAttribute("emailRequestForm") EmailRequestForm emailRequestForm)
            throws MessagingException, UnsupportedEncodingException {
        Map<String, String> resultMap = new HashMap<>();
        boolean hasEmail = memberService.hasDuplicatedEmail(emailRequestForm.getEmail());
        if (hasEmail) {
            resultMap.put("duplicated-email", "이미 존재하는 이메일입니다");
            return ResponseEntity.badRequest().body(resultMap);
        }
        String authCode = memberService.sendAuthCodeByEmail(emailRequestForm.getEmail());
        resultMap.put("authCode", authCode);
        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("pwd/find")
    public String toFindForm (@Valid @ModelAttribute("emailRequestForm") EmailRequestForm emailRequestForm){
        return "/member/find";
    }
    @PostMapping("pwd/find")
    @ResponseBody
    public ResponseEntity sendEmailForFindPwd (@Valid EmailRequestForm emailRequestForm,
                                               BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        Map<String, String> resultMap =new HashMap<>();
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

    @GetMapping("/{memId}/pwd")
    public String pwdUpdateForm() {
        return "/member/myPwdEdit";
    }
    @PatchMapping("/{memId}/pwd")
    @ResponseBody
    public ResponseEntity pwdUpdate(@PathVariable String memId,
                            @Valid @RequestBody final PwdUpdateForm form) {
        boolean isSamePwdAndPwdConfirm = form.getPwd().equals(form.getPwdConfirm());
        if (!isSamePwdAndPwdConfirm) {
            return ResponseEntity.badRequest().body("비밀번호가 일치하지 않습니다");
        }
        memberService.changePwdByMemId(form.getPwd(), memId);
        return ResponseEntity.ok().body("비밀번호 변경에 성공했습니다");
    }

    @GetMapping("/{memId}/profile")
    public String profileEditForm(@Login MemberDto loginMember,
                                  @ModelAttribute("profileForm") ProfileForm profileForm) {
        profileForm.setNickname(loginMember.getNickname());
        profileForm.setLoc(loginMember.getLoc());
        return "/member/myProfileEdit";
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

    @GetMapping("/{memId}/trade/buyList")
    public String myBuyList(@PathVariable String memId, Model model) {
        List<BuyPostDto> myBuyList = tradeCustomRepository.findBuyListByMemId(memId);
        model.addAttribute("buyList", myBuyList);
        return "member/myBuyList";
    }

    @GetMapping("/{memId}/trade/sellList")
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


    @GetMapping("/{memId}/wishes")
    public String wishList(@PathVariable String memId, Model model){
        List<PostDto> postList = postService.selectPostListFromWish(memId);
        model.addAttribute("postList", postList);
        return "myPage/wishList";
    }
}
