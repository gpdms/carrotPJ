package com.exercise.carrotproject.domain.member.util;

import java.util.Random;
import java.util.UUID;

public class GenerateUtils {
    public static String generateEmailAuthCode() {
        String authCode;
        Random random = new Random();
        StringBuffer key = new StringBuffer();
        for(int i=0; i<8; i++) {
            int index = random.nextInt(3);
            switch (index) {
                case 0 :
                    key.append((char) ((int)random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int)random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        authCode = key.toString();
        return authCode;
    }

    public static String generateTempPwd() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 12;
        Random random = new Random();
        String generatedString = random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString ;
    }

    public static String generateUniqueMemId() {
        String uniqueId = UUID.randomUUID().toString();
        uniqueId = uniqueId.replace("-", "");
        uniqueId = uniqueId.substring(0, 16);
        return uniqueId;
    }
}
