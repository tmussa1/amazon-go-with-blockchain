package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * Exception thrown for several validation failures and when an entity is not found
 */
public class AuthenticationServiceException extends Exception {
    private String reason;
    private String fix;

    public AuthenticationServiceException(String reason, String fix) {
        super(reason);
        this.fix = fix;
    }

    public String getReason() {
        return reason;
    }

    public String getFix() {
        return fix;
    }
}
