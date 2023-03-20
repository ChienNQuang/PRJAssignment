/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.order;

import java.io.Serializable;

/**
 *
 * @author Ezarp
 */
public class OrderCheckoutError implements Serializable {
    private String nameLengthError;
    private String addressLengthError;
    private String invalidQuantityError;

    public OrderCheckoutError() {
    }

    /**
     * @return the nameLengthError
     */
    public String getNameLengthError() {
        return nameLengthError;
    }

    /**
     * @param nameLengthError the nameLengthError to set
     */
    public void setNameLengthError(String nameLengthError) {
        this.nameLengthError = nameLengthError;
    }

    /**
     * @return the addressLengthError
     */
    public String getAddressLengthError() {
        return addressLengthError;
    }

    /**
     * @param addressLengthError the addressLengthError to set
     */
    public void setAddressLengthError(String addressLengthError) {
        this.addressLengthError = addressLengthError;
    }

    /**
     * @return the invalidQuantityError
     */
    public String getInvalidQuantityError() {
        return invalidQuantityError;
    }

    /**
     * @param invalidQuantityError the invalidQuantityError to set
     */
    public void setInvalidQuantityError(String invalidQuantityError) {
        this.invalidQuantityError = invalidQuantityError;
    }
}
