package com.exercise.carrotproject.domain.config;

import com.exercise.carrotproject.domain.config.interceptor.KaKaoCodeInterceptor;
import com.exercise.carrotproject.domain.config.interceptor.MemberInfoCheckInterceptor;
import com.exercise.carrotproject.domain.config.interceptor.PostMemberCheckInterceptor;
import com.exercise.carrotproject.web.argumentresolver.LoginMemberArgumentResolver;
import com.exercise.carrotproject.domain.config.interceptor.LoginCheckInterceptor;
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
                .addPathPatterns("/members/**", "/reviews/**", "/post/**")
                .excludePathPatterns("/members/signup/**",  "/members/**/profileImg", "/members/findPwd",
                        "/members/css/**","/members/js/**","/members/assets/**", "/members/error",
                        "/reviews/css/**","/reviews/js/**","/reviews/assets/**", "/reviews/error",
                        "/post/css/**","/post/js/**","/post/assets/**", "/post/error",
                        "/post/board/**", "/post/firstImg/**","post/img/**","post/onSale/**", "post/search");
        registry.addInterceptor(new MemberInfoCheckInterceptor())
                .order(2)
                .addPathPatterns("/members/**")
                .excludePathPatterns("/members/signup/**", "/members/**/profileImg", "/members/findPwd",
                        "/members/css/**","/members/js/**","/members/assets/**",
                        "/members/error");
        registry.addInterceptor(postMemberCheckInterceptor())
                .order(2)
                .addPathPatterns("/post/**")
                .excludePathPatterns("/post/css/**","/post/js/**","/post/assets/**", "/post/error",
                                    "/post/board/**", "/post/firstImg/**","post/img/**","post/onSale/**", "post/search",
                        "/post/detail/**", "/post/addWish", "/post/rmvWish");
        registry.addInterceptor(new KaKaoCodeInterceptor()).addPathPatterns("/login/kakao");
    }

    @Bean
    public PostMemberCheckInterceptor postMemberCheckInterceptor() {
        return new PostMemberCheckInterceptor();
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
