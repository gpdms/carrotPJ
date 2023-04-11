package com.exercise.carrotproject.domain.member.ouath;


import lombok.Data;
import lombok.NoArgsConstructor;

// AccessToken을 활용해 JWT의 Payload 부분인 사용자 정보를 Response받는 VO
//구글에게서 토큰을 받게 된다.
//토큰을 저장할 Vo 객체
@Data
@NoArgsConstructor
public class GoogleLoginResponse {
    private String iss;
    private String azp;
    private String aud;
    private String sub;
    private String email;
    private String emailVerified;
    private String atHash;
    private String name;
    private String picture;
    private String givenName;
    private String familyName;
    private String locale;
    private String iat;
    private String exp;
    private String alg;
    private String kid;
    private String typ;
}

