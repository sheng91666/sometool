package com.sometool.profile;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

public class STHttpProfile {

    public static final String REQ_HTTPS = "https://";

    public static final String REQ_HTTP = "http://";

    public static final String REQ_POST = "POST";

    public static final String REQ_GET = "GET";

    /**
     * Time unit, 1 minute, equals 60 seconds.
     */
    public static final int TM_MINUTE = 60;

    private String reqMethod;

    /**
     * HTTPS or HTTP, currently only HTTPS is valid.
     */
    private String protocol;

    /**
     * Read timeout in seconds.
     */
    private int readTimeout;

    /**
     * Write timeout in seconds
     */
    private int writeTimeout;

    /**
     * Connect timeout in seconds
     */
    private int connTimeout;

    /**
     * http proxy host
     */
    private String proxyHost;

    /**
     * http proxy port
     */
    private int proxyPort;

    /**
     * http proxy user name
     */
    private String proxyUsername;

    /**
     * http proxy password
     */
    private String proxyPassword;

    private SSLSocketFactory sslSocketFactory;

    private X509TrustManager trustManager;


    public STHttpProfile() {
        this.reqMethod = STHttpProfile.REQ_POST;
        this.protocol = STHttpProfile.REQ_HTTPS;
        this.readTimeout = 0;
        this.writeTimeout = 0;
        this.connTimeout = STHttpProfile.TM_MINUTE;
    }

    public STHttpProfile(String protocol, String reqMethod) {
        this.reqMethod = reqMethod;
        this.protocol = protocol;
        this.readTimeout = 0;
        this.writeTimeout = 0;
        this.connTimeout = STHttpProfile.TM_MINUTE;
    }

    public String getReqMethod() {
        return this.reqMethod;
    }

    /**
     * Set request method, GET or POST.
     *
     * @param reqMethod
     */
    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    /**
     * Set read timeout value.
     *
     * @param readTimeout A integer represents time in seconds.
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    /**
     * Set write timeout value.
     *
     * @param writeTimeout A integer represents time in seconds.
     */
    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public int getConnTimeout() {
        return this.connTimeout;
    }

    /**
     * Set connect timeout value.
     *
     * @param connTimeout A integer represents time in seconds.
     */
    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Set request protocol.
     *
     * @param protocol https:// or http://
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyUsername() {
        return proxyUsername;
    }

    public void setProxyUsername(String proxyUsername) {
        this.proxyUsername = proxyUsername;
    }

    public String getProxyPassword() {
        return proxyPassword;
    }

    public void setProxyPassword(String proxyPassword) {
        this.proxyPassword = proxyPassword;
    }


    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public X509TrustManager getX509TrustManager() {
        return trustManager;
    }

    public void setX509TrustManager(X509TrustManager trustManager) {
        this.trustManager = trustManager;
    }

}
