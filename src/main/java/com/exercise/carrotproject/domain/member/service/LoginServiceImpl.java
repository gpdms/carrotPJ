package com.exercise.carrotproject.domain.member.service;

import com.exercise.carrotproject.domain.member.repository.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String loginPwd) {
        return memberRepository.findById(loginId)
                .filter(m -> BCrypt.checkpw(loginPwd, m.getMemPwd()))
                .orElse(null);
    }


//    public GoogleResponse oAuthLogin(String code) throws IOException {
//        //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
//        ResponseEntity<String> accessTokenResponse= googleOauth.requestAccessToken(code);
//        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
//        GoogleOAuthToken oAuthToken=googleOauth.getAccessToken(accessTokenResponse);
//
//        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
//        ResponseEntity<String> userInfoResponse=googleOauth.requestUserInfo(oAuthToken);
//        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
//        GoogleUser googleUser= googleOauth.getUserInfo(userInfoResponse);
//    }


}
