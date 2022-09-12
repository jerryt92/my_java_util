package org.tjl.userTruthCircleDemo.service;

import org.tjl.userTruthCircleDemo.dao.NodeDao;
import org.tjl.userTruthCircleDemo.entity.NodeEntity;

import java.security.GeneralSecurityException;
import java.util.HashMap;

public class NodeService {
    NodeDao nodeDao = new NodeDao();

    public boolean verifyTruthCircle(String id, String password) {
        int nodeCount = nodeDao.countAllNode();
        String headId = id;
        NodeEntity node = new NodeEntity();
        if (nodeCount == 0) {
            System.out.println("信任环为空！");
            return false;
        }
        HashMap<String, NodeEntity> nodeEntityHashMap = nodeDao.getAllNodeHashMap();
        if (nodeEntityHashMap.get(id) == null) {
            System.out.println("找不到初始结点！");
            return false;
        }
        for (int i = 0; i < nodeCount; i ++) {
            node = nodeEntityHashMap.get(id);
            if ( node == null ) {
                System.out.println("信任环不连续！");
                return false;
            }
            if ( !node.verifySelf(password) ) {
                System.out.println("信任环内存在结点验证不通过！");
                return false;
            }
            try {
                password = node.getC_key(password);
                id = node.getC_id();
            } catch (GeneralSecurityException e) {
                return false;
            }
            if (node.getC_id().equals(headId) && i != nodeCount - 1) {
                System.out.println("存在环外节点！");
                return false;
            }
        }
        if( !node.getC_id().equals(headId) ) {
            return false;
        }
        return true;
    }

    public boolean addNode(String inId, String inPassword, String newId, String newPassword) {
        NodeEntity preNode = nodeDao.getNode(inId);
        NodeEntity newNode;
        if (preNode == null) {
            System.out.println("找不到初始结点！");
            return false;
        }
        if ( !preNode.verifySelf(inPassword) ) {
            System.out.println("验证不通过！");
            return false;
        }
        try {
            newNode = new NodeEntity(newId, inId, inPassword, preNode.getC_id(), preNode.getC_key(inPassword), newPassword);
        } catch (GeneralSecurityException e) {
            System.out.println("验证不通过！");
            return false;
        }
        try {
            preNode.setC_id(newId, inPassword);
            preNode.setC_key(newPassword, inPassword);
        } catch (GeneralSecurityException e) {
            System.out.println("验证不通过！");
            return false;
        }
        nodeDao.editNode(preNode);
        nodeDao.addNode(newNode);
        NodeEntity cNode = nodeDao.getNode(newNode.getC_id());
        try {
            cNode.setP_id(newId, newNode.getC_key(newPassword));
            cNode.setP_key(newPassword, newNode.getC_key(newPassword));
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        nodeDao.editNode(cNode);
        return true;
    }
}
