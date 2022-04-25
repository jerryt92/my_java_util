package com.tjl.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
    public static byte[] aesLock(byte[] key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return unlocked
     * @throws GeneralSecurityException
     */
    public static byte[] aesUnlock(byte[] key, byte[] data) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        // 128位密钥 = 16 bytes Key:
        byte[] key = "fc1293f51b7746699534ece17718eae8".getBytes("UTF-8");
        // 加密:
        byte[] data = message.getBytes("UTF-8");
        byte[] locked = aesLock(key, data);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(locked));
        // 解密:
        byte[] unlocked = aesUnlock(key, locked);
        System.out.println("Decrypted: " + new String(unlocked, "UTF-8"));
    }
}