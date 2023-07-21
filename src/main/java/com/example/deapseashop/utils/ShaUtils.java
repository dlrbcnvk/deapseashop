package com.example.deapseashop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtils {

    public static String encryptSHA256(String value) {
        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            byte[] data = md.digest();
            for (byte datum : data) {
                sb.append(String.format("%02x", datum));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
