package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * Gets thrown for invalid tokens. This is a subset of the parent exception
 */
public class AccessDeniedException extends AuthenticationServiceException {

    public AccessDeniedException(String reason, String fix) {
        super(reason, fix);
    }
}
