package com.exercise.carrotproject.domain.member.login;

import com.exercise.carrotproject.domain.member.MemberRepository;
import com.exercise.carrotproject.domain.member.entity.Member;
import com.exercise.carrotproject.domain.member.login.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final MemberRepository memberRepository;

    public Member login(String loginId, String pwd) {
        Member member = memberRepository.findById(loginId)
                .filter(m -> m.getMemPwd().equals(pwd))
                .orElse(null);
        return member;
    }
}
