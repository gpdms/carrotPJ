package com.exercise.carrotproject.domain.member.util;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils{
    public String getHashedPwd(String memPwd) {
        return BCrypt.hashpw(memPwd, BCrypt.gensalt());
    }

}