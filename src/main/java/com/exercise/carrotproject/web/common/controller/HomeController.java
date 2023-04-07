package com.exercise.carrotproject.web.common.controller;


import com.exercise.carrotproject.domain.enumList.Loc;
import com.exercise.carrotproject.domain.member.MemberEntityDtoMapper;
import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberServiceImpl;
import com.exercise.carrotproject.domain.post.repository.PostRepository;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberRepository memberRepository;
    private final MemberServiceImpl memberService;
    private final SecurityUtils securityUtils;
    private final PostRepository postRepository;
    //@PostConstruct
    public void init() {
        Member member3 = Member.builder().memId("tester3").mannerScore(36.5).nickname("3Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester33")).build();
        memberRepository.save(member3);
        Member member2 = Member.builder().memId("tester2").mannerScore(36.5).nickname("2Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester22")).build();
        memberRepository.save(member2);
        Member member1 = Member.builder().memId("tester1").mannerScore(36.5).nickname("1Nick").loc(Loc.GANGBUK).memPwd(securityUtils.getHashedPwd("tester11")).build();
        memberRepository.save(member1);
        Member admin = Member.builder().memId("admin1").mannerScore(36.5).nickname("adminNick").loc(Loc.GANGSEO).memPwd(securityUtils.getHashedPwd("admin1234")).build();
        memberRepository.save(admin);
    }
    @GetMapping("/init")
    public String init(HttpServletRequest request) {
        Member loginMember = memberRepository.findById("tester2").orElse(null);
        MemberDto loginMemberDto = MemberDto.builder().memId(loginMember.getMemId())
                .nickname(loginMember.getNickname())
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

        boolean blockState = false;
        Object loginSession = session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginSession != null) {
            MemberDto loginMember = (MemberDto)loginSession;
            if (memberService.findOneBlockByMemIds(loginMember.getMemId(), member.getMemId()) != null) {
                blockState = true;
            }
        }
        Long countPost = 0L;
        if(!blockState) {
            countPost = postRepository.countByMember(member);
            log.info("countPost{}", countPost);
        }
        model.addAttribute("member", MemberEntityDtoMapper.toMemberDto(member));
        model.addAttribute("countPost", countPost);
        model.addAttribute("blockState", blockState);


        return "memberHome";
    }

}
