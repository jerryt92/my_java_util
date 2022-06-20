package com.tjl.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要工具类
 * @author TianJingli
 */
public class MDUtil {
    /**
     * 信息摘要
     * @param data
     * @param algorithm 可选：MD5 、 SHA-1 、 SHA-256 、 SHA-384 、 SHA-512
     * @return
     */
    public static String getMessageDigest(byte[] data, String algorithm) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(data);
            // 将字节数据转换为十六进制
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) {
        String msg = "11111111111111111111";
        System.out.println(getMessageDigest(msg.getBytes(StandardCharsets.UTF_8),"md5"));
    }
}
