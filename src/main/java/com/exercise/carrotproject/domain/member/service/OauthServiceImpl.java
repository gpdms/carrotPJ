package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.member.ouath.GoogleOauth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OauthServiceImpl{
    private final GoogleOauth googleOuath;


}
