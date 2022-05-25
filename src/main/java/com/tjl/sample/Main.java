package com.tjl.sample;

import com.tjl.util.RandomLockUtil;

public class Main {
    public static void main(String[] args) throws Exception {
        String[] strings = {"tjl", "Tjlrong20000902061X#"};
        System.out.println(strings.length);
        String s = RandomLockUtil.mkArrToString(strings);
        System.out.println(s);
        for (String string : RandomLockUtil.getStringArrFromOneSting(s)) {
            System.out.println("[ " + string + " ]");
        }
    }
}
