/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.registration;

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
public class RegistrationDAO implements Serializable {
    public boolean checkUsernameExist(Connection con, String username) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select username "
                    + "From [Registration] "
                    + "Where username = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            
            rs = stm.executeQuery();
            
            if (rs.next()) {
                return true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
        
        return false;
    }
    
    public RegistrationDTO getRegistrationByUsername(Connection con, String username) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select username, password, lastname, isAdmin "
                    + "From [Registration] "
                    + "Where username = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            
            rs = stm.executeQuery();
            
            if (rs.next()) {
                String lastname = rs.getString("lastname");
                String password = rs.getString("password");
                boolean role = rs.getBoolean("isAdmin");
                
                RegistrationDTO dto = 
                        new RegistrationDTO(username, password, lastname, role);
                
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

    public boolean checkUserValid(Connection con, RegistrationDTO user) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select username, password, lastname, isAdmin "
                    + "From [Registration] "
                    + "Where username = ? and "
                    + "password = ? and "
                    + "lastname = ? and "
                    + "isAdmin = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getPassword());
            stm.setString(3, user.getFullName());
            stm.setBoolean(4, user.isRole());
            
            rs = stm.executeQuery();
            
            return rs.next();
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
    }
    
    private List<RegistrationDTO> registrationList;

    public List<RegistrationDTO> getRegistrationList() {
        return registrationList;
    }
    
    public void searchLastName(Connection con, String searchValue, int page, int size) 
            throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select username, password, lastname, isAdmin "
                    + "From [Registration] "
                    + "Where lastname like ? "
                    + "Order by username "
                    + "Offset ? Rows "
                    + "Fetch Next ? Rows Only";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, "%" + searchValue + "%");
            stm.setInt(2, size * (page - 1));
            stm.setInt(3, size);
            
            rs = stm.executeQuery();
            while (rs.next()) {
                String username = rs.getString("username");
                String password = rs.getString("password");
                String fullName = rs.getString("lastname");
                boolean role = rs.getBoolean("isAdmin");
                
                RegistrationDTO dto = new RegistrationDTO(username, password, fullName, role);
                
                if (this.registrationList == null) {
                    this.registrationList = new ArrayList<>();
                }
                
                this.registrationList.add(dto);
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

    public boolean updateAccount(Connection con, String username, String password, boolean role) 
            throws SQLException {
        PreparedStatement stm = null;
        
        try {
            String sql = "Update [Registration] "
                    + "Set password = ?, isAdmin = ? "
                    + "Where username = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, password);
            stm.setBoolean(2, role);
            stm.setString(3, username);
            
            int affectedRows = stm.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    public boolean deleteRegistration(Connection con, String username) 
            throws SQLException {
        PreparedStatement stm = null;
        
        try {
            String sql = "Delete From [Registration] "
                    + "Where username = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            
            int affectedRows = stm.executeUpdate();
            
            return affectedRows > 0;
        } finally {
            if (stm != null) {
                stm.close();
            }
        }
    }

    public boolean createAccount(Connection con, RegistrationDTO dto) throws SQLException {
        PreparedStatement stm = null;
        boolean result = false;
        
        try {
            if (con != null) {
                //2. Write SQL statement
                String sql = "Insert Into Registration("
                        + "username, password, lastname, isAdmin"
                        + ") Values("
                        + "?, ?, ?, ?"
                        + ")";
                //3. Create Statement Object
                stm = con.prepareStatement(sql);
                stm.setString(1, dto.getUsername());                
                stm.setString(2, dto.getPassword());
                stm.setString(3, dto.getFullName());
                stm.setBoolean(4, dto.isRole());
                //4. Execute Statement to result
                int affectedRows = stm.executeUpdate();
                //5. Process result
                if (affectedRows > 0) {
                    result = true;
                }
                //every object related to db has to be initialized with null and closed under any circumstances
            }//end connection is existed
        } finally {
            if (stm != null) {
                stm.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return result;
    }
    
    public long count(Connection con, String searchValue) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select COUNT(username) As total "
                    + "From [Registration] "
                    + "Where lastname like ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, "%" + searchValue + "%");
            rs = stm.executeQuery();
            
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

    public boolean checkLogin(Connection con, String username, String hashedPassword) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        
        try {
            String sql = "Select username "
                    + "From [Registration] "
                    + "Where username = ? and password = ?";
            
            stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, hashedPassword);
            rs = stm.executeQuery();
            
            if (rs.next()) {
                return true;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            
            if (stm != null) {
                stm.close();
            }
        }
        
        return false;
    }
}
