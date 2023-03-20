/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.order;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author Ezarp
 */
public class OrderDTO implements Serializable {
    private String id;
    private String name;
    private String address;
    private Timestamp date;
    private float total;

    public OrderDTO() {
    }

    public OrderDTO(String id, String name, String address, Timestamp date, float total) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.date = date;
        this.total = total;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the date
     */
    public Timestamp getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Timestamp date) {
        this.date = date;
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

