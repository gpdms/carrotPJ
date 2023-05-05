package com.exercise.carrotproject.domain.config.interceptor;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.post.dto.PostDto;
import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.print.Book;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PostMemberCheckInterceptor implements HandlerInterceptor {
    @Resource
    private PostService postService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        log.info("게시물 인증 체크 인터셉터 실행 {}", request.getRequestURI());
        //쿼리스트링에서 추출 (삭제,숨기기)
        String postIdQ = request.getParameter("postId");
        //pathVariable에서 추출 (수정)
        final Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String postIdP = null;
        if(pathVariables.containsKey("postId")) {
            postIdP = pathVariables.get("postId");
        }
        if (postIdQ == null && postIdP == null) {
             return true;
        }
        HttpSession session = request.getSession(false);
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        boolean equals1 = false;
        if (postIdQ != null) {
            Long postId = Long.valueOf(postIdQ);
            PostDto postDtoQ  = postService.selectOnePost(postId);
            equals1 = loginMember.getMemId().equals(postDtoQ.getMember().getMemId());
        }
        //pathVariable에서 추출 (수정)
        boolean equals2 = false;
        if (postIdP != null) {
            Long postId= Long.valueOf(postIdP);
            PostDto postDtoP = postService.selectOnePost(postId);
            equals2 = loginMember.getMemId().equals(postDtoP.getMember().getMemId());
        }
        if (equals1 || equals2) {
            return true;
        } else {
            log.info("권한 없는 사용자 요청");
            //홈으로 redirect
            response.sendRedirect("/");
            return false;
        }
    }

}
