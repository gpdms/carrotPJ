package com.exercise.carrotproject.domain.member.util;


import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils{
    public static String getHashedPwd(String memPwd) {
        return BCrypt.hashpw(memPwd, BCrypt.gensalt());
    }

    public static void main(String[] args) {
        for(int i = 2; i<=10 ; i++) {
            System.out.println(((i*10)+i)+"getHashedPwd() = " + getHashedPwd("tester"+(i*10)+i));
        }
    }
}
