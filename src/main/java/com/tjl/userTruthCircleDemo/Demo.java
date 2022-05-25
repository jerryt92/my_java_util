package com.tjl.userTruthCircleDemo;

import com.tjl.userTruthCircleDemo.service.NodeService;

public class Demo {
    public static void main(String[] args) {
        NodeService nodeService = new NodeService();
        System.out.println(nodeService.verifyTruthCircle("191107410057", "Tjlrong0902"));
//        nodeService.addNode("tjlrong", "569302", "191107410057", "Tjlrong0902");
    }
}
