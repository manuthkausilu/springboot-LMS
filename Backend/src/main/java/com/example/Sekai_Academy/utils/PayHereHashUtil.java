package com.example.Sekai_Academy.utils;

import java.math.BigInteger;
import java.security.MessageDigest;

public class PayHereHashUtil {

    public static String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);

            // Pad with leading zeros to make 32 chars
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText.toUpperCase(); // PayHere expects uppercase
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

