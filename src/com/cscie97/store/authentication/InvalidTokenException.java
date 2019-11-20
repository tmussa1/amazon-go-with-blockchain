package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * Gets thrown for invalid tokens. This is a subset of the parent exception
 */
public class InvalidTokenException extends AuthenticationServiceException {

    public InvalidTokenException(String reason, String fix) {
        super(reason, fix);
    }
}
