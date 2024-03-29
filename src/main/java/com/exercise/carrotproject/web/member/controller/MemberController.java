package com.exercise.carrotproject.web.member.controller;


import com.exercise.carrotproject.domain.member.dto.JoinNormalMemberRequest;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.dto.UpdateProfileRequest;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.BuyPostDto;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.dto.SoldPostDto;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final PostRepository postRepository;
    private final TradeCustomRepositoryImpl tradeCustomRepository;

    @GetMapping("join")
    public String joinForm(Model model) {
        model.addAttribute("isSocial", false);
        return "member/joinForm";
    }

    @PostMapping("join")
    public ResponseEntity join(@Valid @RequestBody final JoinForm form) {
        if (memberService.hasMemId(form.getMemId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_MEM_ID));
        }
        if (memberService.hasEmail(form.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        JoinNormalMemberRequest member = JoinNormalMemberRequest.builder().memId(form.getMemId())
                .memPwd(form.getPwd())
                .email(form.getEmail())
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        memberService.joinNormalMember(member);
        return ResponseEntity.ok().build();
    }

    @GetMapping("join/memId/{memId}")
    public ResponseEntity memIdDuplicateCheck(@PathVariable String memId) {
        if(memberService.hasMemId(memId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_MEM_ID));
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("join/email/{email}")
    public ResponseEntity emailDuplicateCheck(@PathVariable String email) {
        if(memberService.hasEmail(email)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("join/email/auth-code")
    public ResponseEntity issueAuthCodeByEmail(@Valid @RequestBody final EmailRequestForm form) {
        if(memberService.hasEmail(form.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponse.of(ErrorCode.DUPLICATED_EMAIL));
        }
        String authCode = memberService.issueAuthCodeByEmail(form.getEmail());
        return ResponseEntity.ok().body(authCode);
    }

    @GetMapping("pwd/reset")
    public String toResetPwdForm (){
        return "member/resetPwdForm";
    }

    @PostMapping("pwd/reset")
    public ResponseEntity resetPwd(@Valid @RequestBody final EmailRequestForm form) {
        boolean hasNotEmail = !memberService.hasEmail(form.getEmail());
        if (hasNotEmail) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.of(ErrorCode.NOT_FOUND_EMAIL));
        }
        memberService.resetAndSendTemporaryPwdByEmail(form.getEmail());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/pwd")
    public String toPwdUpdateForm() {
        return "myPage/pwdUpdate";
    }

    @PatchMapping("/{memId}/pwd")
    public ResponseEntity pwdUpdate(@PathVariable String memId,
                                    @Valid @RequestBody final PwdUpdateForm form) {
        boolean isCorrectPwdConfirm = form.getPwd().equals(form.getPwdConfirm());
        if (!isCorrectPwdConfirm) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.NOT_CORRECT_PWD_CONFIRM));
        }
        memberService.changePwdByMemId(form.getPwd(), memId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/settings/profile")
    public String toProfileUpdateForm() {
        return "myPage/profileUpdate";
    }

    @PatchMapping("/{memId}/profile")
    public ResponseEntity profileUpdate(@Valid final ProfileForm form,
                                        @RequestParam("profImg") final MultipartFile profImg,
                                        @Login MemberDto loginMember) {
        if (!memberService.isEmptyOrImageFile(profImg)) {
            return ResponseEntity.badRequest().body(ErrorResponse.of(ErrorCode.NOT_IMG_TYPE));
        }

        UpdateProfileRequest updateRequest = UpdateProfileRequest.builder()
                .memId(form.getMemId())
                .nickname(form.getNickname())
                .loc(form.getLoc()).build();
        memberService.changeProfile(updateRequest, profImg);

        loginMember.setNickname(updateRequest.getNickname());
        loginMember.setLoc(updateRequest.getLoc());
        return ResponseEntity.ok().body(updateRequest);
    }

    @DeleteMapping("/{memId}/profImg")
    public ResponseEntity deleteProfImg(@PathVariable String memId) {
        memberService.deleteProfImg(memId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memId}/trade/buy-list")
    public String myBuyList(@PathVariable String memId, Model model,
                            @PageableDefault(page = 0, size = 15) Pageable pageable) {
        Page<BuyPostDto> myBuyList = tradeCustomRepository.getBuyListPageByMemId(memId, pageable);
        model.addAttribute("buyList", myBuyList);
        return "myPage/buyList";
    }

    @GetMapping("/{memId}/trade/sell-list")
    public String mySellList(@PathVariable String memId, Model model,
                             @PageableDefault(page = 0, size = 15) Pageable pageable,
                             @RequestParam(defaultValue = "onSale") String status){
        //판매중,예약중 게시글
        Page<PostDto> onSaleAndRsvList = postRepository.getOnSalePostPageByMemId(memId, pageable);
        model.addAttribute("onSaleAndRsvList", onSaleAndRsvList);
        //판매완료
        Page<SoldPostDto> soldList = postRepository.getSoldPostPageByMemId(memId, pageable);
        model.addAttribute("soldList", soldList);
        //숨김 게시글
        Page<PostDto> hideList = postRepository.getHiddenPostPageByMemId(memId, pageable);
        model.addAttribute("hideList", hideList);
        model.addAttribute("status", status);
        return "myPage/sellList";
    }

    @GetMapping("/{memId}/wish-list")
    public String myWishList(@PathVariable String memId, Model model){
        List<PostDto> postList = postService.selectPostListFromWish(memId);
        model.addAttribute("postList", postList);
        return "myPage/wishList";
    }
}
