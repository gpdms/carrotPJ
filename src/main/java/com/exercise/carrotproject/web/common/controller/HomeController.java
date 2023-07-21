package com.exercise.carrotproject.web.common.controller;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.enumList.Role;
import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.BlockService;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.service.ReviewService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final BlockService blockService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(@Login MemberDto loginMember,
                       Model model) {
        model.addAttribute("list", postService.selectAllPost(loginMember));
        return "home";
    }

    @GetMapping("/home/{memId}")
    public String toMemberHome(@PathVariable String memId,
                               @Login MemberDto loginMember,
                               Model model){
        boolean isBlocked= false;
        if(loginMember != null) {
            isBlocked = blockService.hasBlockByFromMemToMem(loginMember.getMemId(), memId);
        }

        Long countAllPost = 0L;
        List<PostDto> postListBrief = new ArrayList<>();
        Member homeMember = memberService.findMemberByMemId(memId);
        if(isBlocked == false) {
            countAllPost = postRepository.countByMember(homeMember);
            postListBrief = postService.postListBrief(memId,6);
        }

        Map<ReviewIndicator, Long> positiveMannerBrief = reviewService.getPositiveReviewIndicators(memId, 3L);
        List<ReviewMessageDto> reviewMessageBrief = reviewService.getGoodReviewMessageListByLimit(memId, 3L);
        Long countAllMessages = reviewService.countGoodReviewMessages(memId);

        model.addAttribute("member", MemberEntityDtoMapper.toDto(homeMember));
        model.addAttribute("isBlocked", isBlocked);
        model.addAttribute("postList", postListBrief);
        model.addAttribute("positiveMannerBrief", positiveMannerBrief);
        model.addAttribute("reviewMessageBrief", reviewMessageBrief);
        model.addAttribute("countAllPost", countAllPost);
        model.addAttribute("countAllMessages", countAllMessages);
        return "memberHome";
    }
}
