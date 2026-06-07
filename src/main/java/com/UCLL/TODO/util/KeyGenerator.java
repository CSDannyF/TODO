package com.UCLL.TODO.util;

import java.security.SecureRandom;
import java.util.Base64;

public class KeyGenerator {
    public static void main(String[] args) {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        final var base64key = Base64.getUrlEncoder().withoutPadding().encodeToString(key);
        System.out.println(base64key);
    }
}
