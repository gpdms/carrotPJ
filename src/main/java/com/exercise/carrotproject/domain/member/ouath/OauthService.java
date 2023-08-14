package com.exercise.carrotproject.domain.member.ouath;

import com.exercise.carrotproject.domain.enumList.Role;

public interface OauthService{
    boolean supports(Role role);
    String getAuthorizeUrl();
    OauthToken getOauthToken(OauthCallback oauthCallback);
    OauthUserInfo getUserInfoByToken(OauthToken oauthToken);
    void unlink(String accessToken);
}
