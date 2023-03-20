/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.detail;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ezarp
 */
public class DetailDAO implements Serializable {

    public boolean createDetail(Connection con, String orderId, int sku, int quantity, float price) 
            throws SQLException {
        PreparedStatement stm = null;
        boolean result = false;
        try {
            String sql = "Insert into Detail("
                    + "order_id, sku, quantity, price, total"
                    + ") "
                    + "Values (?, ?, ?, ?, ?)";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, orderId);
            stm.setInt(2, sku);
            stm.setInt(3, quantity);
            stm.setFloat(4, price);
            stm.setFloat(5, price * quantity);
            
            int affectedRows = stm.executeUpdate();
            
            if (affectedRows > 0) {
                result = true;
            }
                    
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
        return result;
    }
    
    private List<DetailDTO> details;

    public List<DetailDTO> getDetails() {
        return details;
    }
    
    public void loadDetailsByOrderId(Connection con, String orderId) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "Select id, sku, quantity, price, total "
                    + "From [Detail] "
                    + "Where order_id = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, orderId);
            rs = stm.executeQuery();
            
            while (rs.next()){
                int id = rs.getInt("id");
                int sku = rs.getInt("sku");
                int quantity = rs.getInt("quantity");
                float price = rs.getFloat("price");
                float total = rs.getFloat("total");
                
                DetailDTO dto = new DetailDTO(id, orderId, sku, quantity, price, total);
                
                if (this.details == null) {
                    this.details = new ArrayList<>();
                }
                
                this.details.add(dto);
            }   
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
    }
}
