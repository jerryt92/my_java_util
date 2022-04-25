package com.tjl.util.security;

public class RandomLockUtil {
    public static String lock(String str) {
        char[] chars = str.toCharArray();
        String ret = "";
        int magic = (int) (Math.random() * 10 + 33);
        ret += (char) magic;
        for (int i = 0;i < magic - 23; i ++) {
            ret += (char)(Math.random() * 65 + 50);
        }
        for (int i = 0;i < chars.length; i ++) {
            chars[i] += (magic - 32);
            ret += chars[i];
        }
        return ret;
    }

    public static String unlock(String str) {
        char[] chars = str.toCharArray();
        String ret = "";
        int flag = chars[0];
        for (int i = 1 + flag - 23;i < chars.length; i ++) {
            chars[i] -= (flag - 32);
            ret += chars[i];
        }
        return ret;
    }

    public static  String mkKey(String username, String password) {
        int checkNumberLen, checkNumber;
        checkNumber = username.length();
        checkNumberLen = ("" + checkNumber).length();
        return "" + checkNumberLen + checkNumber + username + password;
    }
    public static  String trans(String key, int swi) {
        String username = "";
        String password = "";
        String str = "";
        char[] chars = key.toCharArray();
        int checkNumberLen = Integer.parseInt("" + chars[0]);
        int usernameLen;
        for (int i = 1; i <= checkNumberLen; i ++) {
            str += chars[i];
        }
        usernameLen = Integer.parseInt(str);
        if (swi == 1) {
            for (int i = checkNumberLen + 1; i <= usernameLen + checkNumberLen; i ++) {
                username += chars[i];
            }
            return username;
        }
        else if (swi == 2) {
            for (int i = usernameLen + checkNumberLen + 1; i < chars.length; i ++) {
                password += chars[i];
            }
            return password;
        }
        return null;
    }

    public static void main(String[] args) {
        String username = "tjl";
        String password = "123";
        String key = mkKey(username, password);
        String lockStr = lock(key);
        String unlockStr = unlock(lockStr);
        System.out.println("key：" + key);
        System.out.println("加密后：" + lockStr);
        System.out.println("解密后：" + unlockStr);
        System.out.println("username：" + trans(unlockStr, 1));
        System.out.println("password：" + trans(unlockStr, 2));
    }
}
