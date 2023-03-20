/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.cart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Ezarp
 */
public class CartObj implements Serializable{
    private Map</*String, Integer*/Integer, CartItem> items;

    public Map</*String, Integer*/Integer, CartItem> getItems() {
        return items;
    }
    
    public boolean addItemToCart(/*String id*/int sku, String name, int quantity, float price) {
        //1. validate data
//        if (id == null ) {
//            return result;
//        }
//        if (sku.trim().isEmpty()) {
//            return result;
//        }
        
        //2. check existed items
//        if (this.items == null) {
//            this.items = new HashMap<>();
//        }
//        
//        //3. check existed item
//        CartItem item = null;
//        if (!this.items.containsKey(sku)) {
////            int currentQuantity = this.items.get(id);
////            
////            quantity = quantity + currentQuantity;
//            item = new CartItem(name, 0);
//            this.items.put(sku, item);
//        }
//        item = this.items.get(sku);
//        
//        //4. update cart items
//        int currentQuantity = item.getQuantity();
//
//        quantity = quantity + currentQuantity;
//        item.setQuantity(quantity);
//        this.items.put(sku, quantity);
        boolean result = false;
        //1. check validation
//        if (sku <= 0) {
//            return result;
//        }
//        
//        if (quantity <= 0) {
//            return result;
//        }
        
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        
        CartItem item = null;
        if (items.containsKey(sku)) {
            item = items.get(sku);
            int currentQuantity = item.getQuantity();
            item.setQuantity(currentQuantity + quantity);
        } else {
            item = new CartItem(name, quantity, price);
        }
        items.put(sku, item);
        
        result = true;
        return result;
    }
    
    public int getItemQuantity(int sku) {
        int result = 0;
        
        if (sku <= 0) {
            return result;
        }
        
        if (this.items == null) {
            this.items = new HashMap<>();
        }
        
        if (!items.containsKey(sku)) {
            return result;
        }
        
        CartItem item = items.get(sku);
        return item.getQuantity();
    }
    
    public boolean removeItemFromCart(/*String id*/int sku, int quantity) {
        boolean result = false;
        //1. validate data
//        if (sku == null ) {
//            return result;
//        }
        
//        if (sku.trim().isEmpty()) {
//            return result;
//        }
//        if (sku <= 0) {
//            return result;
//        }
//        
//        if (quantity <= 0) {
//            return result;
//        }
        
        //2. check existed items
        if (this.items == null) {
            return result;
        }
        
        //3. check existed item
        if (!this.items.containsKey(sku)) {
            return result;
        }
        
        //4. remove items
        CartItem item = this.items.get(sku);
//        int currentQuantity = this.items.get(sku);
        int currentQuantity = item.getQuantity();
        
        if (currentQuantity >= quantity) {
            quantity = currentQuantity - quantity;
        } else {
            return result;
        }
        
        if (quantity == 0) {
            this.items.remove(sku);
            if (this.items.isEmpty()) {
                this.items = null;
            }
        } else {
            item.setQuantity(quantity);
        }
        result = true;
        
        return result;

    }
//    
//    public boolean updateCart(List<ProductDTO> products) {
//        boolean result = false;
//        
//        if (products == null) {
//            return true;
//        }
//        
//        if (products.isEmpty()){
//            return true;
//        }
//       
//        for (ProductDTO product : products) {
//            int sku = product.getSku();
//            if (items.containsKey(sku)) {
//                CartItem item = items.get(sku);
//                int currentQuantity = product.getQuantity();
//                int cartQuantity = item.getQuantity();
//                if (cartQuantity > currentQuantity) {
//                    
//                }
//                item.setQuantity(currentQuantity - cartQuantity);
//            }//end cart has product
//        }//end traverse products
//        
//        return result;
//    }
}
