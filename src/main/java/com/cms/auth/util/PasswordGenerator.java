package com.cms.auth.util;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String CHAR_SET =
            "ABCDEFGHJKLMNPQRSTUVWXYZ" +   // uppercase
                    "abcdefghijkmnopqrstuvwxyz" + // lowercase
                    "23456789";                  // numbers (no 0,1)

    private static final int PASSWORD_LENGTH = 8;

    private static final SecureRandom random = new SecureRandom();

    public static String generate() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHAR_SET.length());
            password.append(CHAR_SET.charAt(index));
        }

        return password.toString();
    }
}
