package com.exercise.carrotproject.domain.member.ouath;

import com.exercise.carrotproject.domain.member.ouath.kakao.KakaoApiResponse;
import com.exercise.carrotproject.domain.member.ouath.naver.NaverApiResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OauthUserInfo {
    //필수동의 항목
    private final String email;
    //선택동의 항목
    private final String nickname;
    private final String profileImgUrl;

    public static OauthUserInfo fromKakaoApiResponse(KakaoApiResponse response) {
        KakaoApiResponse.KakaoAccount account = response.getKakao_account();
        KakaoApiResponse.KakaoAccount.Profile profile = account.getProfile();

        String profileImgUrl = "";
        String nickname = "";
        if (profile != null) {
            profileImgUrl = profile.getIs_default_image() ? "" :  profile.getProfile_image_url();
            nickname = profile.getNickname();
        }
        return new OauthUserInfo(account.getEmail(), nickname, profileImgUrl);
    }

    public static OauthUserInfo fromNaverApiResponse(NaverApiResponse response) {
        NaverApiResponse.NaverAccount account = response.getResponse();
            return response.getMessage().equals("success") ?
                    new OauthUserInfo(account.getEmail(), account.getNickname(), account.getProfile_image())
                    : new OauthUserInfo("", "", "");
    }
}
