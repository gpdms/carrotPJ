package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.EmailServiceImpl;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.TradeCustomRepository;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewBuyerService;
import com.exercise.carrotproject.domain.review.service.ReviewSellerService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.*;
import com.exercise.carrotproject.web.member.form.memberInfo.MyBuyListForm;
import com.exercise.carrotproject.web.member.form.memberInfo.ProfileForm;
import com.exercise.carrotproject.web.member.form.memberInfo.PwdUpdateForm;
import com.exercise.carrotproject.domain.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;


@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final SecurityUtils securityUtils;
    private final PostServiceImpl postService;
    private final EmailServiceImpl emailService;

    //for Review
    private final TradeCustomRepository tradeCustomRepository;
    private final ReviewSellerService reviewSellerService;
    private final ReviewBuyerService reviewBuyerService;

    @GetMapping("/signup")
    public String signupForm(@ModelAttribute("signupForm") SignupForm form, Model model) {
        model.addAttribute("isSocial", false);
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
        String hashedPwd = securityUtils.getHashedPwd(form.getPwd());
        Member member = Member.builder().memId(form.getMemId())
                .memPwd(hashedPwd)
                .nickname(form.getNickname())
                .role(Role.USER)
                .loc(form.getLoc()).build();
        Map<String, Object> saveResult = memberService.insertMember(member);
        //중복된 아이디 -> field 오류로 알릴 수 있다.
        if(saveResult.containsKey("fail")) {
            if (saveResult.containsValue("id")) {
                bindingResult.rejectValue("memId", "duplicated");
            }
            if (saveResult.containsValue("nickname")) {
                bindingResult.rejectValue("nickname", "duplicated");
            }
            return "/member/signupForm";
        }
        return "redirect:/login";
    }

    @GetMapping("/signup/checkI")
    @ResponseBody
    public ResponseEntity doubleCheckId(@RequestParam String memId) {
        Map<String, String> resultMap = new HashMap<>();
        if(!hasText(memId)) {
            resultMap.put("null","아이디를 입력해주세요.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        boolean hasMemId = memberService.hasDuplicatedMemberId(memId);
        if(hasMemId) {
            resultMap.put("fail","중복된 아이디입니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        resultMap.put("success","사용 가능한 아이디입니다.");
        return ResponseEntity.ok().body(resultMap);
    }
    @GetMapping("/signup/checkN")
    @ResponseBody
    public ResponseEntity doubleCheckNickname(@RequestParam String nickname) {
        Map<String, String> resultMap = new HashMap<>();
        if(!hasText(nickname)) {
            resultMap.put("null","닉네임임을 입력해주세요.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        boolean hasNickname= memberService.hasDuplicatedNickname(nickname);
        if(hasNickname) {
            resultMap.put("fail","중복된 닉네임입니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        resultMap.put("success","사용 가능한 닉네임입니다.");
        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("/{memId}/edit/pwd")
    public String pwdEditForm(@PathVariable String memId,
                                 @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm) {
        return "/member/myPwdEdit";
    }
    @PostMapping("/{memId}/edit/pwd")
    public String pwdUpdate(@PathVariable String memId,
                            @ModelAttribute("profileForm") ProfileForm profileForm,
                            @Valid @ModelAttribute("pwdUpdateForm") PwdUpdateForm pwdUpdateForm, BindingResult bindingResult) {
        Member member = memberService.findMemberForProfileEdit(memId);
        profileForm.setNickname(member.getNickname());
        profileForm.setLoc(member.getLoc());
        if(bindingResult.hasErrors()) {;
            return "/member/myProfileEdit";
        }
        if (!pwdUpdateForm.getPwd().equals(pwdUpdateForm.getPwdConfirm())) {
            bindingResult.rejectValue("pwdConfirm", "pwdConfirmIncorrect", "암호가 일치하지 않습니다.");
            return "/member/myProfileEdit";
        }
        //db에 업데이트 실패 -> 검증오류(PwdUpdateForm에 관한 문제) 아니고, 서버 오류
        //bindingResult에 담아서 보내지 말아야하지 않을까?
        String hashedPwd = securityUtils.getHashedPwd(pwdUpdateForm.getPwd());
        boolean isPwdUpdated = memberService.isPwdUpdated(memId, hashedPwd);
        if (!isPwdUpdated) {
            return "/member/myProfileEdit";
        }
        return "/member/myProfileEdit";
    }

    @GetMapping("/{memId}/profile")
    public String profileEditForm(@PathVariable String memId,
                                  @Login MemberDto loginMember,
                                  @ModelAttribute("profileForm") ProfileForm profileForm) {
        profileForm.setNickname(loginMember.getNickname());
        profileForm.setLoc(loginMember.getLoc());
        return "/member/myProfileEdit";
    }
    @ResponseBody
    @PatchMapping("/{memId}/profile")
    public ResponseEntity profileUpdate(@PathVariable String memId,
                                        @Valid @ModelAttribute("profileForm") ProfileForm profileForm,
                                        @RequestParam("profImg") MultipartFile profImg,
                                        @Login MemberDto loginMember) {
        Map<String, Object> resultMap = new HashMap<>();
        Member updateMember = Member.builder().memId(profileForm.getMemId())
                .nickname(profileForm.getNickname())
                .loc(profileForm.getLoc()).build();
        resultMap = memberService.profileUpdate(updateMember, profImg);
        if(!resultMap.containsKey("success")){
            return ResponseEntity.badRequest().body(resultMap);
        } else {
            Member after = (Member) resultMap.get("success");
            loginMember.setNickname(after.getNickname());
            loginMember.setLoc(after.getLoc());
            Member member = (Member) resultMap.get("success");
            resultMap.clear();
            resultMap.put("member",
                    new ProfileForm(member.getMemId(), member.getProfPath(), member.getNickname(), member.getLoc()));
            return ResponseEntity.ok().body(resultMap);
        }
    }

    //buyList
    @GetMapping("/{memId}/trade/buyList")
    public String buyList(@PathVariable String memId, Model model) {
        List<Trade> buyList = tradeCustomRepository.getBuyList(memId);
        List<MyBuyListForm> buyFormList = new ArrayList<>();
        if(buyList != null) {
            for (Trade buyOne : buyList) {
                Long reviewSellerId = reviewSellerService.findReviewSellerIdByPost(buyOne.getPost());
                MyBuyListForm buyOneForm = new MyBuyListForm(buyOne.getTradeId(), buyOne.getPost(), buyOne.getBuyer().getMemId(), buyOne.getSeller().getMemId(), reviewSellerId);
                buyFormList.add(buyOneForm);
            }
        }
        model.addAttribute("buyList", buyFormList);
        return "member/myBuyList";
    }

    //sellList
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

    //관심목록
    @GetMapping("/{memId}/wishes")
    public String wishList(@PathVariable String memId, Model model){
        List<PostDto> postList = postService.selectPostListFromWish(memId);
        model.addAttribute("postList", postList);
        return "myPage/wishList";
    }

    @PostMapping("signup/emailConfirm")
    @ResponseBody
    public ResponseEntity emailConfirm(@Valid @ModelAttribute("emailRequestForm") EmailRequestForm emailRequestForm) throws MessagingException, UnsupportedEncodingException {
        Map<String, String> resultMap =new HashMap<>();
      /*  if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("email").getDefaultMessage();
            resultMap.put("email", errorMessage);
            return ResponseEntity.badRequest().body(resultMap);
        }*/
        boolean hasEmail = memberService.hasEmailAndRole(emailRequestForm.getEmail(), Role.USER);
        if (hasEmail) {
            resultMap.put("duplicated-email", "이미 존재하는 이메일입니다");
            return ResponseEntity.badRequest().body(resultMap);
        }
        String authCode = emailService.createCode();
        emailService.sendSignupEmail(emailRequestForm.getEmail(), authCode);
        resultMap.put("authCode", authCode);
        return ResponseEntity.ok().body(resultMap);
    }

    @GetMapping("findPwd")
    public String toFindForm (@Valid @ModelAttribute("emailRequestForm") EmailRequestForm emailRequestForm){
        return "/member/find";
    }
    @PostMapping("findPwd")
    @ResponseBody
    public ResponseEntity sendEmailForFindPwd (@Valid @ModelAttribute("emailRequestForm") EmailRequestForm emailRequestForm,
                                               BindingResult bindingResult) throws MessagingException, UnsupportedEncodingException {
        Map<String, String> resultMap =new HashMap<>();
       if(bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError("email").getDefaultMessage();
            resultMap.put("email", errorMessage);
            return ResponseEntity.badRequest().body(resultMap);
        }
        boolean hasEmail = memberService.hasEmailAndRole(emailRequestForm.getEmail(), Role.USER);
        if (!hasEmail) {
            resultMap.put("no-email", "가입하지 않은 이메일입니다.");
            return ResponseEntity.badRequest().body(resultMap);
        }
        long result = memberService.temporaryPwdUdpate(emailRequestForm.getEmail());
        resultMap.put("success", String.valueOf(result));
        return ResponseEntity.ok().body(resultMap);
    }
}
