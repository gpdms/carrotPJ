package com.exercise.carrotproject.web.member;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils{
    public String getHashedPwd(String memPwd) {
        return BCrypt.hashpw(memPwd, BCrypt.gensalt());
    }

}
