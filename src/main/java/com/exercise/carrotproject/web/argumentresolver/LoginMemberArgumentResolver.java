package com.exercise.carrotproject.web.argumentresolver;

import com.exercise.carrotproject.SessionConst;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override //컨트롤러에서 맵핑된 메소드의 파라미터들
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(Login.class);//파라미터에 Login Anootation이 있는지
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());  //파라미터의 타입이 Member인지
        return hasParameterAnnotation && hasMemberType; //true면 resolveArgument 실행
    }

    @Override //컨트롤러에서 맵핑된 메소드의 파라미터를 리턴
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("Login resolveArgument 실행");
        HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);
        if(session == null) {
            return null;
        }
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
