package com.exercise.carrotproject.domain.member;

import com.exercise.carrotproject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService{
    private final MemberRepository memberRepository;
    public Member login(String loginId, String password) {
        Member member = memberRepository.findById(loginId)
                .filter(m -> m.getMemPwd().equals(password))
                .orElse(null);
        return member;
    }
}
