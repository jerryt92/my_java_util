package com.tjl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 信息摘要工具类
 * @author TianJingli
 */
public class MessageDigestUtil {
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
        String msg = "Hello World.";
        // MD5: d7527e2509d7b3035d23dd6701f5d8d0
        // SHA-256: f4bb1975bf1f81f76ce824f7536c1e101a8060a632a52289d530a6f60d52c92
        // SHA-512: fee4e02329c0e1c905d590f4773d8e519ecda859775ac9c83641e3a96c57e7ad461354e486722b6e3c161e493e04f5ef07d9169ff7bdab659d6a57cc316
        System.out.println(getMessageDigest(msg.getBytes(),"md5"));
    }
}
