package com.sometool.profile;

public class STClientProfile {

    private STHttpProfile httpProfile;

    /**
     * If payload is NOT involved in signing process, true means will ignore payload, default is
     * false.
     */
    private boolean unsignedPayload;

    /**
     * valid choices: zh-CN, en-US
     */
    private STLanguage language;
    private boolean debug;

    public STClientProfile(STHttpProfile httpProfile) {
        this.httpProfile = httpProfile;
        this.unsignedPayload = false;
        this.language = null;
        this.setDebug(false);
    }

    public STClientProfile() {
        this(new STHttpProfile());
    }


    public STHttpProfile getHttpProfile() {
        return this.httpProfile;
    }

    public void setHttpProfile(STHttpProfile httpProfile) {
        this.httpProfile = httpProfile;
    }

    /**
     * Get the flag of whether payload is ignored.
     */
    public boolean isUnsignedPayload() {
        return this.unsignedPayload;
    }

    /**
     * Set the flag of whether payload should be ignored. Only has effect when request method is POST.
     *
     * @param flag
     */
    public void setUnsignedPayload(boolean flag) {
        this.unsignedPayload = flag;
    }

    public STLanguage getLanguage() {
        return this.language;
    }

    public void setLanguage(STLanguage lang) {
        this.language = lang;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
