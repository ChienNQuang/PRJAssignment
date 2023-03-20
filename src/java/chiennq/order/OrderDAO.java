/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.order;

import chiennq.utils.StringHelper;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import javax.naming.NamingException;

/**
 *
 * @author Ezarp
 */
public class OrderDAO implements Serializable {

    public OrderDTO createOrder(Connection con, String name, String address) 
            throws SQLException, NamingException {
        PreparedStatement statement = null;
        OrderDTO result = null;
        
        try {
            String sql = "Insert into [Order](id, name, address, date, total) "
                    + "Values(?, ?, ?, ?, 0)";

            String id;
            do {
                id = StringHelper.randomOrderId();
            } while (checkExistId(con, id));

            Timestamp now = Timestamp.from(Instant.now());
            statement = con.prepareStatement(sql);
            statement.setString(1, id);
            statement.setString(2, name);
            statement.setString(3, address);
            statement.setTimestamp(4, now);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                result = new OrderDTO(id, name, address, now, 0);
            }
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        
        return result;
    }
    
    private boolean checkExistId(Connection con, String id) 
            throws SQLException, NamingException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            String sql = "SELECT id "
                    + "From [Order] "
                    + "Where id=?";

            statement = con.prepareStatement(sql);
            statement.setString(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
        }
        
        return false;
    }

    public boolean updateOrderTotalPrice(Connection con, String orderId, float total) 
            throws SQLException {
        PreparedStatement stm = null;
        
        try {
            String sql = "Update [Order] "
                    + "Set total = ? "
                    + "Where id = ?";
            
            stm = con.prepareStatement(sql);
            stm.setFloat(1, total);
            stm.setString(2, orderId);
            
            int affectedRows = stm.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    public OrderDTO getOrderById(Connection con, String orderId) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "SELECT id, name, address, date, total "
                    + "From [Order] "
                    + "Where id=?";

            stm = con.prepareStatement(sql);
            stm.setString(1, orderId);

            rs = stm.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String address = rs.getString("address");
                Timestamp date = rs.getTimestamp("date");
                float total = rs.getFloat("total");
                OrderDTO dto = new OrderDTO(orderId, name, address, date, total);
                
                return dto;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
        
        return null;
    }
}
