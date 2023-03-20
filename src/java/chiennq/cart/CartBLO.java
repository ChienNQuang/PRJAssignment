/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.cart;

import chiennq.product.ProductDAO;
import chiennq.product.ProductDTO;
import chiennq.utils.DBHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.naming.NamingException;

/**
 *
 * @author Ezarp
 */
public class CartBLO {
    private final ProductDAO productDAO;

    public CartBLO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    
    public boolean addItemToCart(CartObj cart, String sku, 
            String quantity, CartAddItemError errors) 
            throws NumberFormatException, SQLException, NamingException{
        Connection con = null;
        
        int skuNumber = Integer.parseInt(sku);
        int quantityNumber = Integer.parseInt(quantity);
        
        if (skuNumber <= 0) {
            return false;
        }
        
        if (quantityNumber <= 0) {
            return false;
        }
        
        ProductDTO product = null;
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                product = productDAO.getProductBySku(con, skuNumber);
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        if (product == null) {
            return false;
        }
        
        String productName = product.getName();
        int cartQuantity = cart.getItemQuantity(skuNumber);
        int productQuantity = product.getQuantity();
        if (cartQuantity + quantityNumber > productQuantity) {
            int remainingQuantity = productQuantity - cartQuantity;
            if (remainingQuantity < 0) {
                remainingQuantity = 0;
            }
            errors.setAddInvalidQuantity(
                            "Product " + productName + " has only " 
                                    + remainingQuantity + " left! You already has " 
                                    + cartQuantity + " in your cart");
            return false;
        }
        
        float roundOff = (float) (Math.round(product.getPrice() * 100.0) / 100.0);
        
        boolean result = cart.addItemToCart(skuNumber, productName, quantityNumber, roundOff);
        
        return result;
    }

    public boolean removeItemsFromCart(CartObj cart, String[] selectedItemIds) 
            throws NumberFormatException {
        if (selectedItemIds == null || selectedItemIds.length <= 0) {
            return false;
        }
        
        Map<Integer, CartItem> items = cart.getItems();
        if (items.isEmpty()) {
            return false;
        }
        
        boolean result = true;
        for (String itemSku : selectedItemIds) {
            int sku = Integer.parseInt(itemSku);
            if (items.containsKey(sku)) {
                boolean removeResult = cart.removeItemFromCart(sku, 1);
                if (!removeResult) {
                    result = false;
                }
            }
        }
        
        return result;
    }
}
