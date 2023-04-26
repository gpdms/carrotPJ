package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Trade;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.TradeCustomRepository;
import com.exercise.carrotproject.domain.post.repository.TradeRepository;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewBuyerServiceImpl;
import com.exercise.carrotproject.domain.review.service.ReviewSellerServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.*;
import com.exercise.carrotproject.web.member.form.memberInfo.MyBuyListForm;
import com.exercise.carrotproject.web.member.form.memberInfo.ProfileForm;
import com.exercise.carrotproject.web.member.form.memberInfo.PwdUpdateForm;
import com.exercise.carrotproject.web.member.form.memberInfo.MySellListForm;
import com.exercise.carrotproject.web.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberServiceImpl memberService;
    private final SecurityUtils securityUtils;
    private final PostServiceImpl postService;

    @Value("${dir.img-profile}")
    private String rootProfileImgDir;

    //for Review
    private final TradeCustomRepository tradeCustomRepository;
    private final ReviewSellerServiceImpl reviewSellerService;
    private final ReviewBuyerServiceImpl reviewBuyerService;

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
        //log.info("saveResult {}", saveResult.containsValue("fail-DM"));
        //중복된 아이디 -> field 오류로 알릴 수 있다.
        if(saveResult.containsKey("fail")) {
            if (saveResult.containsValue("id")) {
                bindingResult.rejectValue("memId", "duplicatedMemId");
            }
            if (saveResult.containsValue("nickname")) {
                bindingResult.rejectValue("nickname", "duplicateNickname");
            }
            return "/member/signupForm";
        }
        return "redirect:/login";
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
    @PostMapping("/{memId}/edit/pwd")
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
        String hashedPwd = securityUtils.getHashedPwd(pwdUpdateForm.getPwd());
        boolean isPwdUpdated = memberService.isPwdUpdated(memId, hashedPwd);
        if (!isPwdUpdated) {
            return  "/member/memberInfo";
        }
        return  "/member/memberInfo";
    }
    @ResponseBody
    @PatchMapping("/{memId}/edit/profile")
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
        if(profPath == null || profPath.isEmpty()) {
            profPath = rootProfileImgDir+"profile_img.png";
        }
        UrlResource urlResource = new UrlResource("file:" + profPath);
        return urlResource;
    }

    //buyList 뽑아오는 것
    @GetMapping("/{memId}/trade/buyList")
    public String buyList(@PathVariable String memId, Model model) {
        List<Trade> buyList = tradeCustomRepository.getBuyList(memId);//나중에 TradeService
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
    /*@GetMapping("/{memId}/trade/sellList")
    public String sellList(@PathVariable String memId, Model model) {
        Member seller= memberService.findOneMember(memId);
        List<Trade> sellList = tradeRepository.findBySeller(seller);
        List<MySellListForm> sellFormList = new ArrayList<>();
        if(sellList != null) {
            for (Trade sellOne : sellList) {
                Long reviewBuyerId = reviewBuyerService.findReviewBuyerIdByPost(sellOne.getPost());
                MySellListForm sellForm = new MySellListForm(sellOne.getTradeId(), sellOne.getPost(), sellOne.getSeller().getMemId(), sellOne.getBuyer().getMemId(), reviewBuyerId);
                sellFormList.add(sellForm);
            }
        }
        model.addAttribute("sellList", sellFormList);
        return "member/mySellList";
    }*/

    //sellList
    @GetMapping("/{memId}/trade/sellList")
    public String mySellList(@PathVariable String memId, Model model){
        //판매중,예약중 게시글
        Map map = postService.selectPostBySellState(memId);
        List<PostDto> onSaleAndRsvList = (List) map.get("onSaleAndRsvList");
        //판매완료
        List<SoldPostDto> soldList = (List) map.get("soldList");
        for (SoldPostDto soldPostDto : soldList) {
            System.out.println("soldPostDto = " + soldPostDto);
        }
        model.addAttribute("onSaleAndRsv", onSaleAndRsvList);
        model.addAttribute("soldList", soldList);

        //숨김 게시글
        List<PostDto> hidePostList = postService.selectHidePost(memId);
        model.addAttribute("hidePostList", hidePostList);

//        log.info("숨김 게시글들>>>>>>>>>{}",hidePostList);

        return "myPage/sellList";
    }


}
