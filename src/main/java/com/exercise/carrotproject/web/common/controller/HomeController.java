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
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
import com.exercise.carrotproject.domain.post.service.PostService;
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
    private final ReviewService reviewService;
    private final PostService postService;
    private final PostRepository postRepository;

    private final SecurityUtils securityUtils;

    //@PostConstruct
    public void testMember() {
        for(int i = 1 ; i <= 5 ; i++) {
            Member member =
                    Member.builder().memId("tester"+i).nickname(i+"Nick")
                            .loc(Loc.GANGBUK)
                            .memPwd(securityUtils.getHashedPwd("tester"+(i*10)+i)).role(Role.USER).build();
            memberRepository.save(member);
        }
        Member hyeeun1 = Member.builder().memId("hyeeun1").mannerScore(365000.0).nickname("혜은").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("hyeeun11")).role(Role.USER).build();
        memberRepository.save(hyeeun1);
        Member Gangnam = Member.builder().memId("tester6").mannerScore(365000.0).nickname("6Nick").loc(Loc.GANGNAM).memPwd(securityUtils.getHashedPwd("tester66")).role(Role.USER).build();
        memberRepository.save(Gangnam);
        Member Gangdong = Member.builder().memId("tester7").mannerScore(365000.0).nickname("7Nick").loc(Loc.GANGDONG).memPwd(securityUtils.getHashedPwd("tester77")).role(Role.USER).build();
        memberRepository.save(Gangdong);
        Member Gangseo = Member.builder().memId("tester8").mannerScore(365000.0).nickname("8Nick").loc(Loc.GANGSEO).memPwd(securityUtils.getHashedPwd("tester88")).role(Role.USER).build();
        memberRepository.save(Gangseo);
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
    public String home(@Login MemberDto loginMember, Model model) {
        model.addAttribute("list", postService.selectAllPost(loginMember));
        return "home";
    }

    @GetMapping("/home/{memId}")
    public String toMemberHome(@PathVariable String memId, Model model,
                               @Login MemberDto loginMember){
        Optional<Member> opMember = memberRepository.findById(memId);
        if(opMember.isEmpty()) {
            return "redirect:/";
        }

        Member member = opMember.orElseThrow();

        boolean isBlocked= false;
        if(loginMember != null) {
            isBlocked = memberService.existBlockByFromMemToMem(loginMember.getMemId(), memId);
        }
        Long countAllPost = 0L;
        List<PostDto> postListBrief = new ArrayList<>();
        if(isBlocked == false) {
            countAllPost = postRepository.countByMember(member);
            postListBrief =  postService.postListBrief(6, memId);
        }
        Map<ReviewIndicator, Long> positiveMannerBrief = reviewService.getPositiveMannerDetailsBrief(memId, 3L);
        List<ReviewMessageDto> reviewMessageBrief =reviewService.goodReviewMessagesBrief(memId, 3L);

        model.addAttribute("member", MemberEntityDtoMapper.toMemberDto(member));
        model.addAttribute("hasBlock", isBlocked);
        model.addAttribute("postList", postListBrief);
        model.addAttribute("positiveMannerBrief", positiveMannerBrief);
        model.addAttribute("reviewMessageBrief", reviewMessageBrief);
        model.addAttribute("countAllPost", countAllPost);
        model.addAttribute("countAllMessages", reviewService.countGoodReviewMessage(memId));
        return "memberHome";
    }

}
