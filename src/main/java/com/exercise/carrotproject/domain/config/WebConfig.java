package com.exercise.carrotproject.domain.config;

import com.exercise.carrotproject.web.argumentresolver.LoginMemberArgumentResolver;
import com.exercise.carrotproject.web.interceptor.*;
import com.exercise.carrotproject.web.argumentresolver.LoginTypeArgumentResolver;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver());
        resolvers.add(loginTypeArgumentResolver());
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
                .addPathPatterns("/members/**", "/reviews/**", "/post/**", "/chat/**", "/blocks/**")
                .excludePathPatterns("/members/join/**", "/members/join-social/**",
                        "/members/**/profileImg", "/members/pwd/reset",
                        "/members/css/**","/members/js/**","/members/assets/**", "/members/error",
                        "/reviews/css/**","/reviews/js/**","/reviews/assets/**", "/reviews/error",
                        "/post/css/**","/post/js/**","/post/assets/**", "/post/error",
                        "/chat/css/**","/chat/js/**","/chat/assets/**", "/chat/error",
                        "/post/board/**", "/post/firstImg/**","post/img/**","post/onSale/**", "post/search");
        registry.addInterceptor(new MemberInfoCheckInterceptor())
                .order(2)
                .addPathPatterns("/members/**", "/blocks/**")
                .excludePathPatterns("/members/join/**", "/members/join-social/**", "/members/**/profileImg",
                        "/members/pwd/reset", "/members/settings/**",  "/blocks",
                        "/members/css/**","/members/js/**","/members/assets/**", "/members/error",
                        "/blocks/css/**","/blocks/js/**","/blocks/assets/**", "/blocks/error");
        registry.addInterceptor(postMemberCheckInterceptor())
                .order(2)
                .addPathPatterns("/post/**")
                .excludePathPatterns("/post/css/**","/post/js/**","/post/assets/**", "/post/error",
                                    "/post/board/**", "/post/firstImg/**","post/img/**","post/onSale/**", "post/search",
                        "/post/detail/**", "/post/addWish", "/post/rmvWish", "/post/uploadPage/**");
    }

    @Bean
    public PostMemberCheckInterceptor postMemberCheckInterceptor() {
        return new PostMemberCheckInterceptor();
    }

    @Bean
    public LoginMemberArgumentResolver loginMemberArgumentResolver() {
        return new LoginMemberArgumentResolver();
    }

    @Bean
    public LoginTypeArgumentResolver loginTypeArgumentResolver() {
        return new LoginTypeArgumentResolver();
    }

    @Bean
    public ServletContextInitializer clearJsession() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
                SessionCookieConfig sessionCookieConfig=servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
            }
        };
    }

}
