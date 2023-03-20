/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.product;

import chiennq.pagination.Page;
import chiennq.utils.DBHelper;
import chiennq.utils.MyAppConstants;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.naming.NamingException;

/**
 *
 * @author Ezarp
 */
public class ProductBLO {
    private final ProductDAO productDAO;

    public ProductBLO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Page<ProductDTO> getAvailableProducts(String page, String size) 
            throws SQLException, NamingException {
        Connection con = null;
        List<ProductDTO> products = null;
        
        int pageNumber;
        if (page == null || page.trim().isEmpty()) {
            pageNumber = MyAppConstants.ShoppingFeatures.DEFAULT_PAGE_NUMBER;
        } else {
            pageNumber = Integer.parseInt(page);
        }
        
        int sizeNumber;
        if (size == null || size.trim().isEmpty()) {
            sizeNumber = MyAppConstants.ShoppingFeatures.DEFAULT_SIZE_NUMBER;
        } else {
            sizeNumber = Integer.parseInt(size);
        }
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                productDAO.loadAvailableProducts(con, pageNumber, sizeNumber);
                products = productDAO.getProducts();
                
                long numberOfProducts = productDAO.count(con);
                int numberOfPages = (int) Math.ceil((double)numberOfProducts / sizeNumber);
                
                for (ProductDTO dto : products) {
                    float roundOff = (float) (Math.round(dto.getPrice() * 100.0) / 100.0);
                    dto.setPrice(roundOff);
                }
                
                Page<ProductDTO> result = new Page<>(products, numberOfPages);
                
                return result;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }   
        
        return null;
    }
    
}
