package com.tjl.sample.aes;

import java.util.Base64;

import static com.tjl.util.security.AESUtil.aesLock;
import static com.tjl.util.security.AESUtil.aesUnlock;

public class AESMain {
    public static void main(String[] args) throws Exception {
        // 原文:
        String message = "Hello, world!";
        System.out.println("Message: " + message);
        // 128位密钥 = 16 bytes Key:
        byte[] key = "aaaaaaaaaaaaaaaa".getBytes("UTF-8");
        // 加密:
        byte[] data = message.getBytes("UTF-8");
        byte[] locked = aesLock(key, data);
        System.out.println("Encrypted: " + Base64.getEncoder().encodeToString(locked));
        // 解密:
        byte[] unlocked = aesUnlock(key, locked);
        System.out.println("Decrypted: " + new String(unlocked, "UTF-8"));
    }
}
