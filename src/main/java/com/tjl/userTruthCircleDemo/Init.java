package com.tjl.userTruthCircleDemo;

import com.tjl.userTruthCircleDemo.dao.NodeDao;
import com.tjl.userTruthCircleDemo.entity.NodeEntity;
import com.tjl.userTruthCircleDemo.entity.User;

import java.util.UUID;

public class Init {
    public static void main(String[] args) throws Exception {
        NodeDao nodeDao = new NodeDao();
        if (nodeDao.countAllNode() != 0) {
            System.out.println("已经进行过初始化操作！");
        } else {
            User user = new User(UUID.randomUUID().toString(), "admin", "admin");
            NodeEntity node = new NodeEntity(user.getId(), user.getId(), user.getPassword(), user.getId(), user.getPassword(), user.getPassword());
            node.showInfo();
            nodeDao.addNode(node);
        }
    }
}
