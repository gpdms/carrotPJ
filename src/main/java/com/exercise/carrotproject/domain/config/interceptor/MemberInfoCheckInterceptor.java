package com.exercise.carrotproject.domain.config.interceptor;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class MemberInfoCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("MemberInfo 인증 체크 인터셉터 실행 {}", requestURI);
        String[] requestUriBits = requestURI.split("/"); // 가져온 uri의 정보를 /기준으로 쪼개기
        String accessibleMemberId = requestUriBits[2];

        HttpSession session = request.getSession(false);
        MemberDto loginMember = (MemberDto) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (loginMember.getMemId().equals(accessibleMemberId)) {
            return true;
        } else {
            log.info("권한 없는 사용자 요청");
            //홈으로 redirect
            response.sendRedirect("/");
            return false;
        }
    }

}
