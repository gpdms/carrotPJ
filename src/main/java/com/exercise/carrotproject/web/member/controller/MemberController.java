package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.dto.BuyListDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SellListDto;
import com.exercise.carrotproject.domain.post.entity.BuyList;
import com.exercise.carrotproject.domain.post.entity.PostEntityDtoMapper;
import com.exercise.carrotproject.domain.post.entity.SellList;
import com.exercise.carrotproject.domain.post.repository.BuyListRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.SellListRepository;
import com.exercise.carrotproject.domain.review.repository.basic.ReviewBuyerRepository;
import com.exercise.carrotproject.domain.review.service.ReviewServiceImpl;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.form.ProfileForm;
import com.exercise.carrotproject.web.member.form.PwdUpdateForm;
import com.exercise.carrotproject.web.member.form.SellListForm;
import com.exercise.carrotproject.web.member.form.SignupForm;
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

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberServiceImpl memberService;
    private final MemberRepository memberRepository;
    private final SecurityUtils securityUtils;
    private final PostRepository postRepository;

    @Value("${dir.img-profile}")
    private String rootProfileImgDir;

    //for Review
    private final SellListRepository sellListRepository;
    private final BuyListRepository buyListRepository;
    private final ReviewServiceImpl reviewService;

    @GetMapping("/{memId}")
    public String toMemberHome(@PathVariable String memId, Model model,
                               HttpSession session){
        Optional<Member> member = memberRepository.findById(memId);
        if(member.isEmpty()) {
            return "redirect:/";
        }

        boolean blockState = false;
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginSession != null) {
            MemberDto loginMember = (MemberDto)loginSession;
            if (memberService.findOneBlockByMemIds(loginMember.getMemId(), member.orElseThrow().getMemId()) != null) {
                blockState = true;
            }
        }

        Long countPost = 0L;
        if(!blockState) {
            countPost = postRepository.countByMember(member.orElse(null));
            log.info("countPost{}", countPost);
        }

        model.addAttribute("member", member.orElse(null));
        model.addAttribute("countPost", countPost);
        model.addAttribute("blockState", blockState);

        return "memberHome";
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
        String hashedPwd = securityUtils.getHashedPwd(form.getPwd());
        Member member = Member.builder().memId(form.getMemId())
                .memPwd(hashedPwd)
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        Map<String, Object> saveResult = memberService.insertMember(member);
        //log.info("saveResult {}", saveResult.containsValue("fail-DM"));
        //중복된 아이디 -> field 오류로 알릴 수 있다.
        if (saveResult.containsValue("fail-DM")) {
            bindingResult.rejectValue("memId", "duplicatedMemId");
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
        String hashedPwd = securityUtils.getHashedPwd(pwdUpdateForm.getPwd());
        boolean isPwdUpdated = memberService.isPwdUpdated(memId, hashedPwd);
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
        if(profPath == null) {
            profPath = rootProfileImgDir+"profile_img.png";
        }
        UrlResource urlResource = new UrlResource("file:" + profPath);
        return urlResource;
    }

    @PostMapping("/{memId}/block")
    public String blockMember(@PathVariable String memId,
                              HttpServletRequest request,
                            Model model){
        HttpSession session = request.getSession(false);
        MemberDto loginMember = (MemberDto)session.getAttribute(SessionConst.LOGIN_MEMBER);
        memberService.insertBlock(loginMember.getMemId(), memId);
        return "redirect:/members/{memId}";
    }

    @PostMapping("/{memId}/cancelBlock")
    public String cancelBlockMember(@PathVariable String memId,
                                    HttpServletRequest request,
                                    @RequestParam String loginMemId){
        memberService.deleteBlock(loginMemId, memId);
        return "redirect:/members/{memId}";
    }

    //buyList
    @GetMapping("/{memId}/buyList")
    private String buyList(@PathVariable String memId, Model model) {
        Member buyer = memberService.findOneMember(memId);
        List<BuyList> buyList = buyListRepository.findByBuyer(buyer);
        List<BuyListDto> buyDtoList = new ArrayList<>();
        for (BuyList buyOne : buyList) {
            BuyListDto buyOneDto = new BuyListDto(buyOne.getBuyId(), buyOne.getPost(), buyOne.getBuyer().getMemId(), buyOne.getSeller().getMemId());
            buyDtoList.add(buyOneDto);
        }
        model.addAttribute("buyList", buyDtoList);

        return "/member/buyList";
    }
    //sellList
    @GetMapping("/{memId}/sellList")
    private String sellList(@PathVariable String memId, Model model) {
        Member seller= memberService.findOneMember(memId);
        List<SellList> sellList = sellListRepository.findBySeller(seller);
        List<SellListForm> sellFormList = new ArrayList<>();
        for (SellList sellOne : sellList) {
            boolean registered = reviewService.isSellerReviewRegistered(sellOne.getPost());
            SellListForm sellForm = new SellListForm(sellOne.getSellId(), sellOne.getPost(), sellOne.getBuyer().getMemId(), sellOne.getSeller().getMemId(), registered);
            sellFormList.add(sellForm);
        }
        model.addAttribute("sellList", sellFormList);
        return "/member/sellList";
    }
}
