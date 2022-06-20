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
     * @param algorithm 可选：MD5-16 、 MD5 、 SHA-1 、 SHA-256 、 SHA-384 、 SHA-512
     * @return
     */
    public static String getMessageDigest(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if (algorithm.toLowerCase().equals("md5-16")) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] bytes = md.digest(data);
                // 将字节数据转换为十六进制
                for (byte b : bytes) {
                    stringBuilder.append(String.format("%02x", b));
                }
                return stringBuilder.toString().substring(8,24);
            } else {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                byte[] bytes = md.digest(data);
                // 将字节数据转换为十六进制
                for (byte b : bytes) {
                    stringBuilder.append(String.format("%02x", b));
                }
                return stringBuilder.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String msg = "11111111111111111111";
        System.out.println(getMessageDigest(msg.getBytes(StandardCharsets.UTF_8),"md5-16"));
    }
}
