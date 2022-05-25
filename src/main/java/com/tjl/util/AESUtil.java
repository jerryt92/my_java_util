package com.tjl.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.Base64;

/**
 * @author TianJingli
 */
public class AESUtil {
    /**
     * AES加密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return locked
     * @throws GeneralSecurityException
     */
    public static String aesLock(String key, String data) throws GeneralSecurityException {
        String locked;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        locked = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return locked;
    }

    /**
     * AES解密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return unlocked
     * @throws GeneralSecurityException
     */
    public static String aesUnlock(String key, String data) throws GeneralSecurityException {
        String unlocked;
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        unlocked = new String(cipher.doFinal(Base64.getDecoder().decode(data)));
        return unlocked;
    }

    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        // 128位密钥 = 16 bytes Key:
        String key = "abcdefghijklmnopabcdefghijklmnop";
        // 加密:
        String locked = aesLock(key, message);
        System.out.println("locked: " + locked);
        // 解密:
        System.out.println("unlock: " + aesUnlock(key, locked));
    }
}