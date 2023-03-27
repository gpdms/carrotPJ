package com.exercise.carrotproject;

import com.exercise.carrotproject.web.argumentresolver.LoginMemberArgumentResolver;
import com.exercise.carrotproject.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
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
                .addPathPatterns("/members/**")
                .excludePathPatterns("/members/signup",
                        "/members/css/**","/members/js/**","/members/assets/**",
                        "/members/error");
    }

}
