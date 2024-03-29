package com.cscie97.store.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author Tofik Mussa
 * A combination of username and password is user for authentication. The password is hashed before it is stored
 */
public class UserNamePassword implements Credential {

    private String userName;
    private String passwordHash;

    /**
     * The password is hashed before getting saved
     * @param userName
     * @param password
     * @throws NoSuchAlgorithmException
     */
    public UserNamePassword(String userName, String password) throws NoSuchAlgorithmException {
        this.userName = userName;
        setPasswordHash(password);
    }

    @Override
    public Credential getCredential() {
        return this;
    }

    /**
     * Computes hash of password
     * @param password
     * @throws NoSuchAlgorithmException
     */
    public void setPasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashVal = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        this.passwordHash = Base64.getEncoder().encodeToString(hashVal);
    }

    public String getUserName() {
        return userName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
