package com.exercise.carrotproject.web.common.controller;


import com.exercise.carrotproject.domain.enumList.ReviewIndicator;
import com.exercise.carrotproject.domain.member.dto.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.service.BlockService;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.domain.review.dto.ReviewMessageDto;
import com.exercise.carrotproject.domain.review.service.ReviewService;
import com.exercise.carrotproject.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberService memberService;
    private final BlockService blockService;
    private final ReviewService reviewService;
    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/")
    public String home(@Login MemberDto loginMember, Model model) {
        List<PostDto> posts = postService.postListBrief(loginMember, 8);
        model.addAttribute("list", posts);
        return "home";
    }

    @GetMapping("/home/{memId}")
    public String toMemberHome(@PathVariable String memId,
                               @Login MemberDto loginMember,
                               Model model){
        boolean isBlocked = false;
        if(loginMember != null && loginMember.getMemId() != memId) {
            isBlocked = blockService.hasBlockByFromMemToMem(loginMember.getMemId(), memId);
        }

        Long countAllPost = 0L;
        List<PostDto> postListBrief = new ArrayList<>();
        Member homeMember = memberService.findMemberByMemId(memId);
        if(isBlocked == false) {
            countAllPost = postRepository.countByMember(homeMember);
            postListBrief = postService.postListBrief(loginMember, 6);
        }

        Map<ReviewIndicator, Long> positiveMannerBrief = reviewService.getPositiveReviewIndicatorsByLimit(memId, 3);
        List<ReviewMessageDto> reviewMessageBrief = reviewService.getAllRecentGoodReviewMessageListByLimit(memId, 3);
        long countAllMessages = reviewService.countAllGoodReviewMessages(memId);

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
