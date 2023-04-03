package com.exercise.carrotproject.domain.config.interceptor;

import com.exercise.carrotproject.domain.member.dto.MemberDto;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static com.exercise.carrotproject.web.common.SessionConst.LOGIN_MEMBER;
import static org.springframework.messaging.simp.stomp.StompHeaders.SESSION;

public class MyHttpSessionHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // 위의 파라미터 중, attributes 에 값을 저장하면 웹소켓 핸들러 클래스의 WebSocketSession에 전달된다
        if (request instanceof ServletServerHttpRequest) {
            System.out.println("Before Handshake");

            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();

            HttpSession httpSession = httpServletRequest.getSession(false);
            MemberDto member = (MemberDto) httpSession.getAttribute(LOGIN_MEMBER);

            attributes.put(SESSION, member);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        System.out.println("After Handshake");
    }
}
