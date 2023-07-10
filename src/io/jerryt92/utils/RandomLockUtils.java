package io.jerryt92.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 可以产生随机的密文，或根据密文解析出原文
 * @author tjl
 */
public class RandomLockUtils {
    /**
     * 生成密文
     * @param str
     * @return
     */
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
        try {
            ret = URLEncoder.encode(ret, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    /**
     * 解出原文
     * @param str
     * @return
     */
    public static String unlock(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        char[] chars = str.toCharArray();
        String ret = "";
        int flag = chars[0];
        for (int i = 1 + flag - 23;i < chars.length; i ++) {
            chars[i] -= (flag - 32);
            ret += chars[i];
        }
        return ret;
    }

    /**
     * 将两个字符串（如用户名和密码）合并为一个字符串
     * @param str1
     * @param str2
     * @return
     */
    public static String mkKey(String str1, String str2) {
        int checkNumberLen, checkNumber;
        checkNumber = str1.length();
        checkNumberLen = ("" + checkNumber).length();
        return "" + checkNumberLen + checkNumber + str1 + str2;
    }

    /**
     * 从上个方法的字符串中解析出str1或str2
     * @param key
     * @param swi （传入1表示解析出str1，2表示解析出str2）
     * @return
     */
    public static  String trans(String key, int swi) {
        String str1 = "";
        String str2 = "";
        String str = "";
        char[] chars = key.toCharArray();
        int checkNumberLen = Integer.parseInt("" + chars[0]);
        int str1Len;
        for (int i = 1; i <= checkNumberLen; i ++) {
            str += chars[i];
        }
        str1Len = Integer.parseInt(str);
        if (swi == 1) {
            for (int i = checkNumberLen + 1; i <= str1Len + checkNumberLen; i ++) {
                str1 += chars[i];
            }
            return str1;
        }
        else if (swi == 2) {
            for (int i = str1Len + checkNumberLen + 1; i < chars.length; i ++) {
                str2 += chars[i];
            }
            return str2;
        }
        return null;
    }

    public static String mkArrToString(String[] strings) throws Exception {
        String ret = "";
        int arrSizeNumberLen,arrSizeNumber;
        arrSizeNumber = strings.length;
        arrSizeNumberLen = ( "" + arrSizeNumber ).length();
        if (arrSizeNumber > 9) // 一般不太可能，以防万一
        {
            throw new Exception("Arr too big!");
        }
        ret += arrSizeNumberLen;
        ret += arrSizeNumber;
        for (String elementStr : strings) {
            ret += ( "" + elementStr.length() ).length();
            ret += elementStr.length();
            ret += elementStr;
        }
        return ret;
    }

    public static String[] getStringArrFromOneSting(String string) throws Exception {
        char[] stringCharArr = string.toCharArray();
        int arrSizeNumberLen = Integer.parseInt("" + stringCharArr[0]);
        int arrSizeNumber = 0;
        switch (arrSizeNumberLen) {
            case 1 : {
                arrSizeNumber = Integer.parseInt("" + stringCharArr[1]);
                break;
            }
            case 2 : {
                arrSizeNumber = Integer.parseInt("" + stringCharArr[1] + "" + stringCharArr[2]);
                break;
            }
            default : {
                throw new Exception("Illegal String!");
            }
        }
        String[] strings = new String[arrSizeNumber];
        int elementHeadIndex = arrSizeNumberLen + 1;
        int elementSizeNumberLen = 0;
        int elementSizeNumber = 0;
        for (int i = 0; i < arrSizeNumber; i ++) {
            elementSizeNumberLen = Integer.parseInt( "" + stringCharArr[elementHeadIndex] );
            switch (elementSizeNumberLen) {
                case 1 : {
                    elementSizeNumber = Integer.parseInt("" + stringCharArr[elementHeadIndex + 1]);
                    break;
                }
                case 2 : {
                    elementSizeNumber = Integer.parseInt("" + stringCharArr[elementHeadIndex + 1] + "" + stringCharArr[elementHeadIndex + 2]);
                    break;
                }
                default : {
                    throw new Exception("Illegal String!");
                }
            }
            String elementStr = "";
            for (int j = elementHeadIndex + elementSizeNumberLen + 1; j < elementHeadIndex + elementSizeNumberLen + elementSizeNumber + 1; j ++) {
                elementStr += stringCharArr[j];
            }
            strings[i] = elementStr;
            elementHeadIndex += elementSizeNumberLen + elementSizeNumber + 1;
        }
        return strings;
    }

    /**
     * Demo
     * @param args
     */
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