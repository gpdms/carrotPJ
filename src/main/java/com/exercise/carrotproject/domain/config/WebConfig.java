package com.exercise.carrotproject.domain.config;

import com.exercise.carrotproject.web.argumentresolver.LoginMemberArgumentResolver;
import com.exercise.carrotproject.domain.config.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      /*  registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/**", "/*.ico", "/error");*/

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/members/**/edit");
//                .excludePathPatterns("/members/signup",
//                        "/members/css/**","/members/js/**","/members/assets/**",
//                        "/members/error");
    }

}
