package com.ctrip.framework.cerberus.client.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES256Helper {

    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    /**
     * 加密
     *
     * @param plaintext
     * @param key
     * @return
     */
    public static String encrypt(String plaintext, String key) {
        if (plaintext == null || plaintext.trim().isEmpty()) {
            return plaintext;
        }
        try {
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            if (raw.length < 16) {
                throw new IllegalArgumentException("Invalid key size.");
            }
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
            byte[] cipherBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(cipherBytes);
        } catch (Exception e) {
            // fail
            return plaintext;
        }
    }

    /**
     * 解密
     *
     * @param cipherText
     * @param key
     * @return
     */
    public static String decrypt(String cipherText, String key) {
        if (cipherText == null || cipherText.trim().isEmpty()) {
            return cipherText;
        }
        try {
            byte[] cipherBytes = Base64.getDecoder().decode(cipherText);
            byte[] raw = key.getBytes(StandardCharsets.UTF_8);
            if (raw.length < 16) {
                throw new IllegalArgumentException("Invalid key size.");
            }
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
            byte[] original = cipher.doFinal(cipherBytes);
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            //fail
            return cipherText;
        }
    }
}
