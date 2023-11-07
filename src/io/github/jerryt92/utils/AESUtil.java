package io.github.jerryt92.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * AES工具类
 *
 * @author jerryt92.github.io
 * @date 2022/6/20
 */
public class AESUtil {

    // ECB模式不需要iv
    private static IvParameterSpec iv;

    static {
        try {
            iv = new IvParameterSpec(
                MDUtil.transMd5To16(MDUtil.getMessageDigest("tjlaes2022".getBytes(StandardCharsets.UTF_8), "MD5"))
                    .getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * AES加密
     *
     * @param key  AES密钥必须为16字节(AES-128)或32字节(AES-256)
     * @param data
     * @return locked
     * @throws GeneralSecurityException
     */
    public static byte[] aesEncrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data);
    }

    /**
     * AES解密
     *
     * @param key         AES密钥必须为16字节(AES-128)或32字节(AES-256)
     * @param encryptData
     * @return unlocked
     * @throws GeneralSecurityException
     */
    public static byte[] aesDecrypt(byte[] encryptData, byte[] key) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        return cipher.doFinal(encryptData);
    }

    /**
     * byte[]转十六进制字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }

    /**
     * 十六进制字符串转byte[]
     *
     * @param hexString
     * @return
     */
    public static byte[] hexToBytes(String hexString) {
        // 检查输入是否为空或者长度不是偶数
        if (hexString == null || hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hexadecimal String");
        }
        // 创建一个字节数组，长度为字符串长度的一半
        byte[] bytes = new byte[hexString.length() / 2];
        // 遍历字符串，每两个字符转换为一个字节
        for (int i = 0; i < hexString.length(); i += 2) {
            // 获取当前字符的十六进制值
            int firstDigit = Character.digit(hexString.charAt(i), 16);
            // 获取下一个字符的十六进制值
            int secondDigit = Character.digit(hexString.charAt(i + 1), 16);
            // 检查是否有非法字符
            if (firstDigit == -1 || secondDigit == -1) {
                throw new IllegalArgumentException("Invalid hexadecimal String");
            }
            // 将两个十六进制值合并为一个字节
            bytes[i / 2] = (byte)((firstDigit << 4) + secondDigit);
        }
        // 返回字节数组
        return bytes;
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
        String encrypted =
            Base64.getEncoder().encodeToString(aesEncrypt(message.getBytes(StandardCharsets.UTF_8), key.getBytes()));
        System.out.println("encrypted: " + encrypted);
        // 解密:
        System.out.println("decrypted: " + new String(
            aesDecrypt(Base64.getDecoder().decode(encrypted), key.getBytes(StandardCharsets.UTF_8)), "UTF8"));
    }
}