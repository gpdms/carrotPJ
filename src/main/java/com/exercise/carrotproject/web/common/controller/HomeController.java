package com.exercise.carrotproject.web.common.controller;


import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Block;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.service.PostServiceImpl;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.service.ReviewService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import com.exercise.carrotproject.domain.member.util.SecurityUtils;
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
    private final MemberService memberService;
    private final PostServiceImpl postService;
    private final SecurityUtils securityUtils;
    private final PostRepository postRepository;
    private final ReviewService reviewService;

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
        List<PostDto> postList = postService.selectAllPost(loginMember);
        model.addAttribute("list", postList);
        return "home";
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
            hasBlock = memberService.existBlockByFromMemToMem(loginMember.getMemId(), memId);
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
