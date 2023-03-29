package com.exercise.carrotproject.domain.member.login;

import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String loginPwd) {
        return memberRepository.findById(loginId)
                .filter(m -> BCrypt.checkpw(loginPwd, m.getMemPwd()))
                .orElse(null);
    }
}
