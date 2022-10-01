package org.tjl.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author TianJingli
 */
public class AESUtil {

    // ECB模式不需要iv
    private static IvParameterSpec iv;
    static {
        try {
            iv = new IvParameterSpec(MDUtil.transMd5To16(MDUtil.getMessageDigest("tjlaes2022".getBytes(StandardCharsets.UTF_8),"MD5")).getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES加密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param data
     * @return locked
     * @throws GeneralSecurityException
     */
    public static byte[] aesEncrypt(byte[] data,byte[] key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key,"AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     * @param key AES密钥必须为16位(AES-128)或32位(AES-256)
     * @param encryptData
     * @return unlocked
     * @throws GeneralSecurityException
     */
    public static byte[] aesDecrypt(byte[] encryptData,byte[] key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key,"AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptData);
    }

    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        // 128位密钥 = 16 bytes Key:
//        String key = UUID.randomUUID().toString().replace("-","").substring(0, 16);
        String key = MDUtil.getMessageDigest("123".getBytes(StandardCharsets.UTF_8), "MD5");
        System.out.println("key = " + key);
        // 加密:
        String encrypted = Base64.getEncoder().encodeToString(aesEncrypt(message.getBytes(StandardCharsets.UTF_8),key.getBytes()));
        System.out.println("encrypted: " + encrypted);
        // 解密:
        System.out.println("decrypted: " + new String(aesDecrypt(Base64.getDecoder().decode(encrypted),key.getBytes(StandardCharsets.UTF_8)),"UTF8"));
    }
}