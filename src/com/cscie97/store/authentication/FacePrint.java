package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * A customer's face print can be used for authentication alternatively
 */
public class FacePrint implements Credential{

    private String facePrint;

    public FacePrint(String facePrint) {
        this.facePrint = facePrint;
    }

    @Override
    public Credential getCredential() {
        return this;
    }

    public String getFacePrint() {
        return facePrint;
    }
}
