package com.tjl.sample.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static com.tjl.util.AESUtil.aesEncrypt;
import static com.tjl.util.AESUtil.aesDecrypt;

public class AESMain {
    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        // 128位密钥 = 16 bytes Key:
        String key = "abcdefghijklmnopabcdefghijklmnop";
        // 加密:
        String locked = Base64.getEncoder().encodeToString(aesEncrypt(message.getBytes(StandardCharsets.UTF_8),key.getBytes(StandardCharsets.UTF_8)));
        System.out.println("locked: " + locked);
        // 解密:
        System.out.println("unlock: " + new String(aesDecrypt(Base64.getDecoder().decode(locked), key.getBytes(StandardCharsets.UTF_8))));
    }
}
