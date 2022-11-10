package org.tjl.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要工具类
 *
 * @author JerryT
 */
public class MDUtil {
    /**
     * 信息摘要
     *
     * @param data
     * @param algorithm 可选：MD2 / MD5 / SHA-1 / SHA-224 / SHA-256 / SHA-384 / SHA-512
     * @return messageDigest
     */
    public static String getMessageDigest(byte[] data, String algorithm) throws NoSuchAlgorithmException {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(data);
            // 将字节数据转换为十六进制
            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 将32字节的MD5转换为16字节
     * @param md5_32B
     * @return
     */
    public static String transMd5To16(String md5_32B) {
        return md5_32B.substring(8, 24);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String msg = "Hello World.";
        System.out.println(getMessageDigest(msg.getBytes(StandardCharsets.UTF_8), "sha-256"));
        System.out.println(transMd5To16(getMessageDigest(msg.getBytes(StandardCharsets.UTF_8), "md5")));
    }
}
