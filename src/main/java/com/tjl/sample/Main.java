package com.tjl.sample;

import com.tjl.util.RandomLockUtil;

import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
        for (byte b : "a".getBytes()) {
            System.out.println(Integer.toBinaryString(b));
        }
        System.out.println(Integer.toBinaryString('a'));
    }
}
