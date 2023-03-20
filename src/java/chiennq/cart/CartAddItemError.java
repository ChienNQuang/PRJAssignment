/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.cart;

/**
 *
 * @author Ezarp
 */
public class CartAddItemError {
    private String addInvalidQuantity;

    public CartAddItemError() {
    }

    /**
     * @return the addInvalidQuantity
     */
    public String getAddInvalidQuantity() {
        return addInvalidQuantity;
    }

    /**
     * @param addInvalidQuantity the addInvalidQuantity to set
     */
    public void setAddInvalidQuantity(String addInvalidQuantity) {
        this.addInvalidQuantity = addInvalidQuantity;
    }
}
