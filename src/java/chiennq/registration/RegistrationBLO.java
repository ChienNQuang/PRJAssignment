/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.registration;

import chiennq.pagination.Page;
import chiennq.utils.DBHelper;
import chiennq.utils.MyAppConstants;
import chiennq.utils.SecurityHelper;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author Ezarp
 */
public class RegistrationBLO {
    private final RegistrationDAO registrationDAO;

    public RegistrationBLO(RegistrationDAO registrationDAO) {
        this.registrationDAO = registrationDAO;
    }
    
    public RegistrationDTO login(String username, String password) 
            throws NoSuchAlgorithmException, SQLException, NamingException {
        Connection con = null;
        boolean validate = true;
        
        if (!validate) return null;
        
        String hashedPassword = SecurityHelper.hash(password);
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                RegistrationDTO user = registrationDAO.getRegistrationByUsername(con, 
                                                    username);
                
                if (user != null && user.getPassword().equals(hashedPassword)) {
                    return user;
                } else {
                    return null;
                }
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return null;
    }
    
    public boolean checkUserValid(RegistrationDTO user) 
            throws SQLException, NamingException {
        Connection con = null;
        
        if (user == null) {
            return false;
        }
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                return registrationDAO.checkUserValid(con, user);
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return false;
    }
    
    public Page<RegistrationDTO> searchLastName(String searchValue, String page, String size) 
            throws SQLException, NamingException {
        Connection con = null;
        Page<RegistrationDTO> result = null;
        
        if (searchValue == null || searchValue.trim().isEmpty()) {
            return null;
        }
        
        int pageNumber;
        if (page == null || page.trim().isEmpty()) {
            pageNumber = MyAppConstants.SearchAccountFeatures.DEFAULT_PAGE_NUMBER;
        } else {
            pageNumber = Integer.parseInt(page);
        }
        
        int sizeNumber;
        if (size == null || size.trim().isEmpty()) {
            sizeNumber = MyAppConstants.SearchAccountFeatures.DEFAULT_SIZE_NUMBER;
        } else {
            sizeNumber = Integer.parseInt(size);
        }
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                registrationDAO.searchLastName(con, searchValue, pageNumber, sizeNumber);
                List<RegistrationDTO> searchResult = registrationDAO.getRegistrationList();
                long totalRows = registrationDAO.count(con, searchValue);
                
                if (searchResult != null) {
                    int totalPages = (int) Math.ceil((double)totalRows / sizeNumber);
                    result = new Page<>(searchResult, totalPages);
                }
                
                return result;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return null;
    }

    public boolean updateAccountRole(String username, boolean role) 
            throws SQLException, NamingException {
        Connection con = null;
        
        if (username == null) {
            return false;
        }
        
        String password = null;
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                RegistrationDTO user = registrationDAO.getRegistrationByUsername(con, username);
                
                password = user.getPassword();
                
                boolean result = registrationDAO.updateAccount(con, username, password, role);
                
                return result;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return false;
    }

    public boolean deleteAccount(String username) 
            throws SQLException, NamingException {
        Connection con = null;
        
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                boolean result = registrationDAO.deleteRegistration(con, username);
                
                return result;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return false;
    }

    public RegistrationDTO getRegistrationByUsername(String username) 
            throws SQLException, NamingException {
        Connection con = null;
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                RegistrationDTO user = registrationDAO.getRegistrationByUsername(con, username);
                
                return user;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return null;
    }
    
    private boolean validateCreateInput(String username, String password, String confirm, String fullname, RegistrationCreateError errors) {
        boolean result = true;
        if (username.trim().length() < 6 ||
                username.trim().length() > 20) {
            errors.setUsernameLengthError("Username has to be from 6 to 20 characters");
            result = false;
        }//end username does not obey required length

        if (password.trim().length() < 6 ||
                password.trim().length() > 30) {
            errors.setPasswordLengthError("Password has to be from 6 to 30 characters");
            result = false;
        }//end password does not obey required length 
        else if (!confirm.trim().equals(password.trim())) {
            errors.setConfirmNotMatched("Confirm must match password");
            result = false;
        }//end confirm does not obey required criteria

        if (fullname.trim().length() < 2 ||
                fullname.trim().length() > 50) {
            errors.setFullnameLengthError("Full name has to be from 2 to 50 characters");
            result = false;
        }//end fullname does not obey required length 
        
        return result;
    }

    public boolean createAccount(String username, String password, String confirm, String fullname, RegistrationCreateError errors) 
            throws NoSuchAlgorithmException, SQLException, NamingException {
        boolean validate = validateCreateInput(username, password, confirm, fullname, errors);
        
        if (!validate) return false;
        String hashedPassword = SecurityHelper.hash(password);
        
        RegistrationDTO dto = new RegistrationDTO(username, hashedPassword, fullname, false);
        Connection con = DBHelper.getConnection();
        boolean result = registrationDAO.createAccount(con, dto);
        
        return result;
    }

    public RegistrationDTO checkLogin(String username, String hashedPassword) throws SQLException, NamingException {
        Connection con = null;
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                boolean result =  registrationDAO.checkLogin(con, username, hashedPassword);
                
                if (result) {
                    return registrationDAO.getRegistrationByUsername(con, username);
                }
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return null;
    }
}
