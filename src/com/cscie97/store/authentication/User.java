package com.cscie97.store.authentication;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Tofik Mussa
 * A user object with id and credentials
 */
public class User implements Visitable {

    private String userId;
    private List<Credential> credentials;
    private List<Entitlement> entitlements;
    Logger logger = Logger.getLogger(User.class.getName());

    /**
     * constructor
     * @param userId
     */
    public User(String userId) {
        this.userId = userId;
        this.credentials = new ArrayList<>();
        this.entitlements = new ArrayList<>();
    }

    /**
     * Constructor
     */
    public User() {
        this.credentials = new ArrayList<>();
        this.entitlements = new ArrayList<>();
    }

    /**
     * Adds credential to user
     * @param credential
     */
    public void addCredentials(Credential credential){
        this.credentials.add(credential);
    }

    /**
     * Adds entitlement to user
     * @param entitlement
     */
    public void addEntitlements(Entitlement entitlement){
        this.entitlements.add(entitlement);
    }

    /**
     * Finds a credential associated for a user. Then checks if the usernames match and if the hash of the passwords
     * match
     * @param userName
     * @param passWord
     * @return whether credentials match
     * @throws NoSuchAlgorithmException
     * @throws AccessDeniedException
     */
    public boolean checkCredentials(String userName, String passWord) throws NoSuchAlgorithmException, AccessDeniedException {
        List<UserNamePassword> userNamePasswords = credentials.stream()
                .filter(credential -> credential instanceof UserNamePassword)
                .map(credential -> (UserNamePassword) credential)
                .collect(Collectors.toList());
        Optional<UserNamePassword> userNamePwd = userNamePasswords.stream()
                .filter(credential -> credential.getUserName().equals(userName))
                .findFirst();
        if(userNamePwd.isEmpty() || !computePasswordHash(passWord).equals(userNamePwd.get().getPasswordHash())){
            throw new AccessDeniedException("Failed to verify credentials", "Please try again");
        }
        return true;
    }

    /**
     * Since a user can't have more than one voice and face print associated with a user id ,
     * we can safely assume that what ever is found can be used for comparison
     * @param voiceFacePrint
     * @return whether face or voice prints match
     */
    public boolean checkCredentials(String voiceFacePrint){
        VoicePrint voicePrint = (VoicePrint) credentials.stream()
                .filter(credential -> credential instanceof VoicePrint)
                .collect(Collectors.toList()).get(0);
        FacePrint facePrint = (FacePrint) credentials.stream()
                .filter(credential -> credential instanceof FacePrint)
                .collect(Collectors.toList()).get(0);
        return voiceFacePrint.equals(voicePrint.getVoicePrint()) ||
                voiceFacePrint.equals(facePrint.getFacePrint());
    }


    /**
     * Computes hash of a password
     * @param password
     * @return hash of password
     * @throws NoSuchAlgorithmException
     */
    public String computePasswordHash(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashVal = messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashVal);
    }

    /**
     * Users are vistable
     * @param ivisitor
     */
    @Override
    public void accept(Ivisitor ivisitor) {
        ivisitor.visit(this);
    }

    public List<Entitlement> getEntitlements() {
        return entitlements;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User with user id " + userId;
    }
}
