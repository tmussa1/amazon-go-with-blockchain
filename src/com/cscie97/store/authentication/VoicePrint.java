package com.cscie97.store.authentication;

/**
 * @author Tofik Mussa
 * A alternative credential to username and password
 */
public class VoicePrint implements Credential {

    private String voicePrint;

    public VoicePrint(String voicePrint) {
        this.voicePrint = voicePrint;
    }

    @Override
    public Credential getCredential() {
        return this;
    }

    public String getVoicePrint() {
        return voicePrint;
    }
}
