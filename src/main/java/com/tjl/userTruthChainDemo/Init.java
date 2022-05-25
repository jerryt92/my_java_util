package com.tjl.userTruthChainDemo;

import com.tjl.userTruthChainDemo.entity.NodeEntity;
import com.tjl.userTruthChainDemo.entity.User;
import com.tjl.util.AESUtil;
import com.tjl.util.MDUtil;
import com.tjl.util.RandomLockUtil;

import java.util.Base64;
import java.util.UUID;

public class Init {
    public static void main(String[] args) throws Exception {
        User user = new User(UUID.randomUUID().toString(), "admin", "admin");
        NodeEntity node = new NodeEntity(user.getId(), user.getPassword(), user.getId(), user.getPassword(), user.getId(), user.getPassword());
        node.showInfo();
        boolean b1 = node.verifySelf("admin");
        System.out.println(b1);
        node.setC_id("3");
        boolean b2 = node.verifySelf("admin");
        System.out.println(b2);
    }
}
