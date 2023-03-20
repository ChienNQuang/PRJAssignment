/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chiennq.registration;

import java.io.Serializable;

/**
 *
 * @author Ezarp
 */
public class RegistrationDeleteError implements Serializable {
    private String deleteSelfError;

    public RegistrationDeleteError() {
    }

    /**
     * @return the deleteSelfError
     */
    public String getDeleteSelfError() {
        return deleteSelfError;
    }

    /**
     * @param deleteSelfError the deleteSelfError to set
     */
    public void setDeleteSelfError(String deleteSelfError) {
        this.deleteSelfError = deleteSelfError;
    }
}
