/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.order;

import chiennq.receipt.ReceiptObj;
import chiennq.cart.CartItem;
import chiennq.cart.CartObj;
import chiennq.receipt.ReceiptProductDetail;
import chiennq.detail.DetailDAO;
import chiennq.detail.DetailDTO;
import chiennq.product.ProductDAO;
import chiennq.product.ProductDTO;
import chiennq.utils.DBHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author Ezarp
 */
public class OrderBLO {
    private final ProductDAO productDAO;
    private final OrderDAO orderDAO;
    private final DetailDAO detailDAO;
    private final Logger LOGGER = Logger.getLogger(OrderBLO.class);

    public OrderBLO(ProductDAO productDAO, OrderDAO orderDAO, DetailDAO detailDAO) {
        this.productDAO = productDAO;
        this.orderDAO = orderDAO;
        this.detailDAO = detailDAO;
    }

    public String checkout(CartObj cart, String name, String address, OrderCheckoutError errors) 
            throws NamingException, SQLException {
        boolean validate = true;
        if (name.isEmpty()) {
            errors.setNameLengthError("Name must not be blank!");
            validate = false;
        }
        
        if (address.isEmpty()) {
            errors.setAddressLengthError("Address must not be blank!");
            validate = false;
        }
        
        if (!validate) return null;
        
        if (name.length() > 50) {
            errors.setNameLengthError("Name length exceeded the limit!");
            validate = false;
        }
        
        if (address.length() > 250) {
            errors.setAddressLengthError("Address length exceeded the limit!");
            validate = false;
        }
        
        if (!validate) return null;
        
        Map<Integer, CartItem> items = cart.getItems();
        
        Connection con = null;
        
        try {
            con = DBHelper.getConnection();
            if (con != null) {
                con.setAutoCommit(false);
                OrderDTO order = orderDAO.createOrder(con, name, address);
                String orderId = order.getId();
                
                productDAO.loadProductsOfSkus(con, items.keySet());
                List<ProductDTO> products = productDAO.getProducts();
                
                float total = 0;
                //create detail for each item in cart
                for (ProductDTO product : products) {
                    //get DB for product quantity
                    int productQuantity = product.getQuantity();
                    //get quantity for product in cart
                    int cartQuantity = items.get(product.getSku()).getQuantity();
                    //check if product has no sufficient quantity
                    if (productQuantity < cartQuantity) {
                        errors.setInvalidQuantityError("Product " + product.getName() + " does not have enough quantity!");
                        con.rollback();
                        return null;
                    }
                    //create detail 
                    float productTotal = product.getPrice() * cartQuantity;
                    boolean detailCreated = detailDAO.createDetail(con, orderId, product.getSku(), cartQuantity, product.getPrice());
                    boolean updateResult = productDAO.updateQuantity(con, product.getSku(), productQuantity - cartQuantity);
                    //if cant create then rollback
                    if (!detailCreated || !updateResult) {
                        con.rollback();
                        return null;
                    }
                    total = total + productTotal;
                }
                
                boolean result = orderDAO.updateOrderTotalPrice(con, orderId, total);
                
                if (result) {
                    con.commit();
                    return orderId;
                } else {
                    con.rollback();
                }
            }
        } catch (SQLException ex) {
            LOGGER.error(ex);
            if (con != null) {
                con.rollback();
                return null;
            }
        } finally {
            if (con != null) {
                con.close();
            }
        }
        
        return null;
    }

    public ReceiptObj makeReceipt(String orderId) 
            throws SQLException, NamingException {
        Connection con = null;
        ReceiptObj result = null;
        
        try {
            con = DBHelper.getConnection();
            
            if (con != null) {
                OrderDTO order = orderDAO.getOrderById(con, orderId);
                
                if (order == null) {
                    return null;
                }
                
                detailDAO.loadDetailsByOrderId(con, orderId);
                List<DetailDTO> details = detailDAO.getDetails();
                
                if (details == null) {
                    return null;
                }
                
                Set<Integer> keySet = new HashSet<>();
                details.forEach((dto) -> {
                    keySet.add(dto.getSku());
                });
                
                productDAO.loadProductsOfSkus(con, keySet);
                List<ProductDTO> products = productDAO.getProducts();
                
                if (products == null) {
                    return null;
                }
                
                List<ReceiptProductDetail> productDetails = new ArrayList<>();
                products.forEach((product) -> {
                    details.stream().filter((detail) -> (product.getSku() == detail.getSku())).forEachOrdered((detail) -> {
                        ReceiptProductDetail pD =
                                new ReceiptProductDetail(detail.getId(),
                                        detail.getSku(), product.getName(),
                                        detail.getQuantity(), detail.getPrice(),
                                        detail.getTotal());
                        productDetails.add(pD);
                    });
                });
                
                result = new ReceiptObj(order, productDetails);
                
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
