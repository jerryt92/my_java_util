package org.tjl.userTruthCircleDemo.dao;

import org.tjl.userTruthCircleDemo.entity.NodeEntity;
import org.tjl.userTruthCircleDemo.jdbcutil.JdbcUtil;

import java.sql.*;
import java.util.Date;
import java.util.HashMap;

public class NodeDao {

    public boolean addNode(NodeEntity node) {
        try {
            Connection connection = JdbcUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareCall("insert into truth_circle values (?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, node.getId());
            preparedStatement.setString(2, node.getP_id());
            preparedStatement.setString(3, node.getP_key());
            preparedStatement.setString(4, node.getC_id());
            preparedStatement.setString(5, node.getC_key());
            preparedStatement.setString(6, node.getAesData());
            preparedStatement.setTimestamp(7, new Timestamp(new Date().getTime()));
            preparedStatement.setTimestamp(8, new Timestamp(new Date().getTime()));
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int countAllNode() {
        int count = 0;
        try {
            ResultSet resultSet = JdbcUtil.getStatement().executeQuery("select * from truth_circle");
            while(resultSet.next()){
                count ++;
            }
            return count;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public NodeEntity getNode(String id) {
        NodeEntity node = null;
        try {
            PreparedStatement preparedStatement = JdbcUtil.getConnection().prepareStatement("select * from truth_circle where id = ?");
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                node = new NodeEntity();
                node.setId(resultSet.getString("id"));
                node.setP_id(resultSet.getString("p_id"));
                node.setP_key(resultSet.getString("p_key"));
                node.setC_id(resultSet.getString("c_id"));
                node.setC_key(resultSet.getString("c_key"));
                node.setAesData(resultSet.getString("aes_data"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return node;
    }

    public HashMap<String, NodeEntity> getAllNodeHashMap() {
        HashMap<String, NodeEntity> nodeEntityHashMap = new HashMap<>();
        try {
            ResultSet resultSet = JdbcUtil.getStatement().executeQuery("select * from truth_circle");
            while (resultSet.next()) {
                NodeEntity node = new NodeEntity();
                node.setId(resultSet.getString("id"));
                node.setP_id(resultSet.getString("p_id"));
                node.setP_key(resultSet.getString("p_key"));
                node.setC_id(resultSet.getString("c_id"));
                node.setC_key(resultSet.getString("c_key"));
                node.setAesData(resultSet.getString("aes_data"));
                nodeEntityHashMap.put(node.getId(), node);
            }
            return nodeEntityHashMap;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean editNode(NodeEntity node) {
        try {
            Connection connection = JdbcUtil.getConnection();
            PreparedStatement preparedStatement = connection.prepareCall("update truth_circle set id = ?, p_id = ?, p_key = ?, c_id = ?, c_key = ?, aes_data = ?, edit_time = ? where id = ?");
            preparedStatement.setString(1, node.getId());
            preparedStatement.setString(2, node.getP_id());
            preparedStatement.setString(3, node.getP_key());
            preparedStatement.setString(4, node.getC_id());
            preparedStatement.setString(5, node.getC_key());
            preparedStatement.setString(6, node.getAesData());
            preparedStatement.setTimestamp(7, new Timestamp(new Date().getTime()));
            preparedStatement.setString(8, node.getId());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
