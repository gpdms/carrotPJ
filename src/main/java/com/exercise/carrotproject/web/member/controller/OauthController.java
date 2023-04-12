package com.exercise.carrotproject.web.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Controller
@RequiredArgsConstructor
public class AuthController {
    @GetMapping(value = "/auth/callback")
    public BaseResponse<GetSocialOAuthRes> callback (
            @RequestParam(name = "code") String code)throws IOException, BaseException{
        System.out.println(">> 소셜 로그인 API 서버로부터 받은 code :"+ code);
        SocialLoginType socialLoginType= SocialLoginType.valueOf(socialLoginPath.toUpperCase());
        GetSocialOAuthRes getSocialOAuthRes=oAuthService.oAuthLogin(socialLoginType,code);
        return new BaseResponse<>(getSocialOAuthRes);
    }
}
