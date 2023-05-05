package com.exercise.carrotproject.web.common.controller;


import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.repository.PostRepositoryImpl;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.repository.ReviewDetailCustomRepository;
import com.exercise.carrotproject.domain.review.service.ReviewServiceImpl;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.web.member.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberRepository memberRepository;
    private final MemberServiceImpl memberService;
    private final PostServiceImpl postService;
    private final SecurityUtils securityUtils;
    private final PostRepository postRepository;
    private final ReviewServiceImpl reviewService;

    @PostConstruct
    public void init() {
        Member member3 = Member.builder().memId("tester3").mannerScore(365000.0).nickname("3Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester33")).role(Role.USER).build();
        memberRepository.save(member3);
        Member member2 = Member.builder().memId("tester2").mannerScore(365000.0).nickname("2Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester22")).role(Role.USER).build();
        memberRepository.save(member2);
        Member member1 = Member.builder().memId("tester1").mannerScore(365000.0).nickname("1Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester11")).role(Role.USER).build();
        memberRepository.save(member1);
        Member admin = Member.builder().memId("admin1").mannerScore(365000.0).nickname("adminNick").loc(Loc.GANGSEO).memPwd(securityUtils.getHashedPwd("admin1234")).role(Role.ADMIN).build();
        memberRepository.save(admin);
    }
    @GetMapping("/init")
    public String init(HttpServletRequest request) {
        Member loginMember = memberRepository.findById("tester1").orElse(null);
        MemberDto loginMemberDto = MemberDto.builder().memId(loginMember.getMemId())
                .nickname(loginMember.getNickname())
                .role(Role.USER)
                .mannerScore(loginMember.getMannerScore())
                .loc(loginMember.getLoc()).build();
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMemberDto);
        return "redirect:/";
    }

    @GetMapping("/")
    public String home(@Login MemberDto loginMember, Model model,
                       RedirectAttributes redirectAttributes ) {
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }
        //세션이 유지되면 로그인된 홈으로 이동
        redirectAttributes.addAttribute("memId", loginMember.getMemId());
        model.addAttribute("member", loginMember);
        return "redirect:/home/{memId}";
    }

    @GetMapping("/home/{memId}")
    public String toMemberHome(@PathVariable String memId, Model model,
                               HttpSession session){
        Optional<Member> opMember = memberRepository.findById(memId);
        if(opMember.isEmpty()) {
            return "redirect:/";
        }
        Member member = opMember.orElseThrow();

        boolean hasBlock = false;
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember != null) {
            hasBlock = memberService.existBlockByMemIds(loginMember.getMemId(), member.getMemId());
        }
        System.out.println("hasBlock!!!!! = " + hasBlock);
        Long countReviewMessage = countReviewMessage = reviewService.countGoodReviewMessage(memId);
        Map<ReviewIndicator, Long> positiveMannerBrief = reviewService.getPositiveMannerDetailsBrief(memId, 3L);
        List<ReviewMessageDto> reviewMessageBrief =reviewService.goodReviewMessagesBrief(memId, 3L);

        Long countPost = 0L;
        List<PostDto> postListBrief = new ArrayList<>();
        if(hasBlock == false) {
            countPost = postRepository.countByMember(member);
            postListBrief =  postService.postListBrief(6, memId);
        }
        model.addAttribute("member", MemberEntityDtoMapper.toMemberDto(member));
        model.addAttribute("hasBlock", hasBlock);
        model.addAttribute("postList", postListBrief);
        model.addAttribute("countPost", countPost);
        model.addAttribute("positiveMannerBrief", positiveMannerBrief);
        model.addAttribute("countReviewMessage", countReviewMessage);
        model.addAttribute("reviewMessageBrief", reviewMessageBrief);
        return "memberHome";
    }

}
