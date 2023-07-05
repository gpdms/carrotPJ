package com.exercise.carrotproject.domain.member.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils{
    public static String encrpytPwd(String memPwd) {
        return BCrypt.hashpw(memPwd, BCrypt.gensalt());
    }
    public static boolean isSamePlainPwdAndHashedPwd(String plainPwd, String hashedPwd){
        return BCrypt.checkpw(plainPwd, hashedPwd);
    }
}
