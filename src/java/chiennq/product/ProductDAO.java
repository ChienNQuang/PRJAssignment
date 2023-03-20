/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.product;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Ezarp
 */
public class ProductDAO implements Serializable {
    
    private List<ProductDTO> products;

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void loadAvailableProducts(Connection con, int page, int size) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //2. Create SQL statement
            String sql = "Select sku, name, description, price, quantity, status "
                    + "From Product "
                    + "Where status=1 and quantity > 0 "
                    + "Order by sku "
                    + "Offset ? Rows "
                    + "Fetch Next ? Rows Only";

            //3. Create statement
            stm = con.prepareStatement(sql);
            stm.setInt(1, size * (page - 1));
            stm.setInt(2, size);

            //4. Execute statement to get ResultSet
            rs = stm.executeQuery();

            //5. Process result
            while (rs.next()) {
                int sku = rs.getInt("sku");
                String name = rs.getString("name");
                String description = rs.getString("description");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                boolean isAvailable = rs.getBoolean("status");
                ProductDTO dto = new ProductDTO(sku, name, description, price, quantity, isAvailable);

                if (products == null) {
                    products = new ArrayList<>();
                }//end product list has not existed

                products.add(dto);
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
    
    public ProductDTO getProductBySku(Connection con, int sku) 
            throws SQLException {
        ProductDTO result = null;
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select sku, name, description, price, quantity, status "
                    + "From [Product] "
                    + "Where sku = ?"; 
            
            stm = con.prepareStatement(sql);
            stm.setInt(1, sku);
            
            rs = stm.executeQuery();
            if (rs.next()) {
                String name = rs.getString("name");
                String description = rs.getString("description");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                boolean available = rs.getBoolean("status");
                
                ProductDTO dto = new ProductDTO(sku, name, description, price, quantity, available);
                
                result = dto;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
        
        return result;
    }

    public void loadProductsOfSkus(Connection con, Set<Integer> keySet) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select sku, name, description, price, quantity, status "
                    + "From [Product] "
                    + "Where sku In (" + questionMarks(keySet.size()) + ")";
            
            stm = con.prepareStatement(sql);
//            stm.setString(1, joinKeySet(keySet));
            prepareStatementKeySet(stm, keySet);
            
            rs = stm.executeQuery();
            
            while (rs.next()) {
                int sku = rs.getInt("sku");
                String name = rs.getString("name");
                String description = rs.getString("description");
                float price = rs.getFloat("price");
                int quantity = rs.getInt("quantity");
                boolean isAvailable = rs.getBoolean("status");
                ProductDTO dto = new ProductDTO(sku, name, description, price, quantity, isAvailable);

                if (products == null) {
                    products = new ArrayList<>();
                }//end product list has not existed

                products.add(dto);
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
    
    private void prepareStatementKeySet(PreparedStatement stm, Set<Integer> keySet) throws SQLException {
        int i = 1;
        for (int sku : keySet) {
            stm.setInt(i++, sku);
        }
    }
    
    private String questionMarks(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("?");
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }
    
    private String joinKeySet(Set<Integer> keySet) {
        StringBuilder sb = new StringBuilder();
        for (int key : keySet) {
            sb.append(key);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
        return sb.toString();
    }

    public boolean updateQuantity(Connection con, int sku, int quantity) throws SQLException {
        PreparedStatement stm = null;
       
        try {
            String sql = "Update [Product] "
                   + "Set quantity = ? "
                   + "Where sku = ?";
           
            stm = con.prepareStatement(sql);
            stm.setInt(1, quantity);
            stm.setInt(2, sku);
            
            int affectedRows = stm.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    public long count(Connection con) throws SQLException {
        Statement stm = null;
        ResultSet rs = null;
        try {
            String sql = "Select count(sku) as total "
                    + "From [Product] "
                    + "Where status=1 and quantity > 0";
           
            stm = con.createStatement();
            rs = stm.executeQuery(sql);
            
            if (rs.next()) {
                long total = rs.getLong("total");
                
                return total;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
        }
        
        return 0;
    }
}

