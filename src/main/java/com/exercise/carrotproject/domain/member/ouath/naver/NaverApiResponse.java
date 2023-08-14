package com.exercise.carrotproject.domain.member.ouath.naver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverApiResponse {
    private String resultcode;
    private String message;
    private NaverAccount response;

    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NaverAccount {
        private String id;
        private String email;
        private String nickname;
        private String profile_image;
    }
}

