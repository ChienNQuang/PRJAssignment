/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.receipt;

import chiennq.order.OrderDTO;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Ezarp
 */
public class ReceiptObj implements Serializable {
    private OrderDTO order;
    private List<ReceiptProductDetail> productDetail;


    public ReceiptObj() {
    }
    
    public ReceiptObj(OrderDTO order, List<ReceiptProductDetail> productDetail) {
        this.order = order;
        this.productDetail = productDetail;
    }

    /**
     * @return the order
     */
    public OrderDTO getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(OrderDTO order) {
        this.order = order;
    }

    /**
     * @return the productDetail
     */
    public List<ReceiptProductDetail> getProductDetail() {
        return productDetail;
    }

    /**
     * @param productDetail the productDetail to set
     */
    public void setProductDetail(List<ReceiptProductDetail> productDetail) {
        this.productDetail = productDetail;
    }
}
