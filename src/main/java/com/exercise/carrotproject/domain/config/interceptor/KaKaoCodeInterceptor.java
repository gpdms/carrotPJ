package com.exercise.carrotproject.domain.config.interceptor;

import com.exercise.carrotproject.domain.post.service.PostService;
import com.exercise.carrotproject.web.common.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Slf4j
public class KaKaoCodeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String revokedCode = (String) session.getAttribute(SessionConst.KAKAO_REVOKED_CODE);
        String code = request.getParameter("code");
        if (revokedCode!=null && revokedCode.equals(code)) {
            session.removeAttribute(SessionConst.KAKAO_ACCESS_TOKEN);
            response.sendRedirect("/login");
            return false;
        }
        return true;
    }
}
