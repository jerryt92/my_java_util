package com.tjl.sample.security;

import java.util.Base64;

import static com.tjl.util.AESUtil.aesLock;
import static com.tjl.util.AESUtil.aesUnlock;

public class AESMain {
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
