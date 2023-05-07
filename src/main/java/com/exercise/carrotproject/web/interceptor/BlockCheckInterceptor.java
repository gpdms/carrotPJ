package com.exercise.carrotproject.web.interceptor;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.repository.BlockRepository;
import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.service.MemberService;
import com.exercise.carrotproject.domain.post.entity.Post;
import com.exercise.carrotproject.domain.post.repository.PostRepository;

import com.exercise.carrotproject.web.common.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Slf4j
public class BlockCheckInterceptor implements HandlerInterceptor {
    @Resource
    private PostRepository postRepository;
    @Resource
    private MemberRepository memberRepository;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("게시물 차단 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession(false);
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if(loginMember == null) {
            return true;
        }
        String[] requestUriBits = requestURI.split("/");
        String postIdS = requestUriBits[3];
        Long postId = Long.valueOf(postIdS);
        Post post = postRepository.findById(postId).orElse(null);
        String postMemId = "";
        boolean hasBlock = false;
        if (post != null) {
            postMemId = post.getMember().getMemId();
            log.info("postMemId {}", postMemId);
            log.info("loginMemId {}", loginMember.getMemId());
            hasBlock = memberRepository.hasBlockByMemIds(postMemId, loginMember.getMemId());
            log.info("boolean hasBlock {}", hasBlock);
        }
        if(!hasBlock) {
            return true;
        } else {
            log.info("차단 사용자 요청");
            response.sendRedirect("/");
            return false;
        }
    }

}
