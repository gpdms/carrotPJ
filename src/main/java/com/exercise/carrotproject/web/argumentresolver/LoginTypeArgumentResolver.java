package com.exercise.carrotproject.web.argumentresolver;

import com.exercise.carrotproject.domain.enumList.Role;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginTypeArgumentResolver implements HandlerMethodArgumentResolver {

    @Override //컨트롤러에서 맵핑된 메소드의 파라미터들
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");
        boolean hasParameterAnnotation = parameter.hasParameterAnnotation(LoginType.class);
        boolean hasMemberType = Role.class.isAssignableFrom(parameter.getParameterType());
        return hasParameterAnnotation && hasMemberType;
    }

    @Override //컨트롤러에서 맵핑된 메소드의 파라미터를 리턴
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("LoginType resolveArgument 실행");
        if (parameter.getParameterType().equals(Role.class)) {
            String platform = webRequest.getParameter("platform");
            if (platform != null) {
                return platform.equals("kakao") ? Role.SOCIAL_KAKAO :
                        platform.equals("naver") ? Role.SOCIAL_NAVER : Role.NORMAL;
            }
        }
        return WebArgumentResolver.UNRESOLVED;
    }
}
