package com.sometool;

import com.sometool.exception.STException;

public class STCredential {

    private String secretId;
    private String secretKey;
    private String token;
    private Updater updater;

    public STCredential() {
    }

    public STCredential(String token) {
        this.token = token;
    }

    public STCredential(String secretId, String secretKey) {
        this(secretId, secretKey, "");
    }

    public STCredential(String secretId, String secretKey, String token) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.token = token;
    }

    public STCredential(String secretId, String secretKey, String token, Updater updater) {
        this.secretId = secretId;
        this.secretKey = secretKey;
        this.token = token;
        this.updater = updater;
    }

    public Updater getUpdater() {
        return updater;
    }

    public void setUpdater(Updater updater) {
        this.updater = updater;
    }

    public String getSecretId() {
        tryUpdate();
        return this.secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        tryUpdate();
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken() {
        tryUpdate();
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private void tryUpdate() {
        if (updater == null) {
            return;
        }

        try {
            updater.update(this);
        } catch (STException e) {
            // wrap as RuntimeException to keep API consistent
            throw new RuntimeException(e);
        }
    }

    public interface Updater {
        void update(STCredential credential) throws STException;
    }
}
