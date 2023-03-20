/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.detail;

import java.io.Serializable;

/**
 *
 * @author Ezarp
 */
public class DetailDTO implements Serializable {
    private int id;
    private String orderId;
    private int sku;
    private int quantity;
    private float price;
    private float total;
    
    public DetailDTO() {
    }

    public DetailDTO(int id, String orderId, int sku, int quantity, float price, float total) {
        this.id = id;
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the sku
     */
    public int getSku() {
        return sku;
    }

    /**
     * @param sku the sku to set
     */
    public void setSku(int sku) {
        this.sku = sku;
    }

    /**
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public float getPrice() {
        return (float) (Math.round(price * 100.0) / 100.0);
    }

    /**
     * @param price the price to set
     */
    public void setPrice(float price) {
        this.price = price;
    }

    /**
     * @return the total
     */
    public float getTotal() {
        return (float) (Math.round(total * 100.0) / 100.0);
    }

    /**
     * @param total the total to set
     */
    public void setTotal(float total) {
        this.total = total;
    }
}
