package com.sometool;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.sometool.exception.STException;
import com.sometool.http.STHttpConnection;
import com.sometool.model.STJsonResponseErrModel;
import com.sometool.model.STJsonResponseModel;
import com.sometool.model.STSSEResponseModel;
import com.sometool.profile.STClientProfile;
import com.sometool.profile.STHttpProfile;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class STAbstractClient {
    public static final int HTTP_RSP_OK = 200;
    public Gson gson;
    private STCredential credential;
    private STClientProfile profile;

    private STLog log;

    private STHttpConnection httpConnection;


    public STAbstractClient(STCredential credential, String path) {
        this(credential, new STClientProfile());
    }

    public STAbstractClient(
            STCredential credential,
            STClientProfile profile) {
        this.credential = credential;
        this.profile = profile;
        this.log = new STLog(getClass().getName(), profile.isDebug());
        this.httpConnection = new STHttpConnection(
                this.profile.getHttpProfile().getConnTimeout(),
                this.profile.getHttpProfile().getReadTimeout(),
                this.profile.getHttpProfile().getWriteTimeout()
        );
        this.httpConnection.addInterceptors(this.log);
        this.trySetProxy(this.httpConnection);
        this.trySetSSLSocketFactory(this.httpConnection);
    }

    private void trySetProxy(STHttpConnection conn) {
        String host = this.profile.getHttpProfile().getProxyHost();
        int port = this.profile.getHttpProfile().getProxyPort();

        if (host == null || host.isEmpty()) {
            return;
        }
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        conn.setProxy(proxy);

        final String username = this.profile.getHttpProfile().getProxyUsername();
        final String password = this.profile.getHttpProfile().getProxyPassword();
        if (username == null || username.isEmpty()) {
            return;
        }
        conn.setProxyAuthenticator(
                new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        String credential = Credentials.basic(username, password);
                        return response
                                .request()
                                .newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                    }
                });
    }

    private void trySetSSLSocketFactory(STHttpConnection conn) {
        SSLSocketFactory sslSocketFactory = this.profile.getHttpProfile().getSslSocketFactory();
        X509TrustManager trustManager = this.profile.getHttpProfile().getX509TrustManager();
        if (sslSocketFactory != null) {
            if (trustManager != null) {
                this.httpConnection.setSSLSocketFactory(sslSocketFactory, trustManager);
            } else {
                this.httpConnection.setSSLSocketFactory(sslSocketFactory);
            }
        }
    }


    protected <T> T internalRequest(HashMap<String, Object> request, String method, Class<T> typeOfT) throws STException {
        STCircuitBreaker.Token breakerToken = null;
        try {
            Response resp = internalRequestRaw(request, method);
            if (Objects.equals(resp.header("Content-Type"), "text/event-stream")) {
                return processResponseSSE(resp, typeOfT, breakerToken);
            }
            return processResponseJson(resp, typeOfT, breakerToken);
        } catch (IOException e) {
            if (breakerToken != null) {
                breakerToken.report(false);
            }
            throw new STException("", e);
        }
    }


    protected Response internalRequestRaw(HashMap<String, Object> request, String method) throws STException, IOException {
        Response okRsp = null;
        if (ObjectUtils.isNotEmpty(request)) {
            okRsp = doRequest(request, method);
        } else {
            throw new STException("参数不能是空");
        }
        if (okRsp.code() != STAbstractClient.HTTP_RSP_OK) {
            String msg = "response code is " + okRsp.code() + ", not 200";
            log.info(msg);
            throw new STException(msg, "", "ServerSideError");
        }
        return okRsp;
    }

    private Response doRequest(HashMap<String, Object> param, String method) throws STException, IOException {
        String strParam = this.formatRequestData(param);
        String reqMethod = this.profile.getHttpProfile().getReqMethod();
        String protocol = this.profile.getHttpProfile().getProtocol();
        String url = protocol + method;

        if (reqMethod.equals(STHttpProfile.REQ_GET)) {
            return this.httpConnection.getRequest(url + "?" + strParam);
        } else if (reqMethod.equals(STHttpProfile.REQ_POST)) {
            return this.httpConnection.postRequest(url, strParam);
        } else {
            throw new STException("Method only support (GET, POST)");
        }
    }

    private String formatRequestData(Map<String, Object> param) throws STException {
//        param.put("Timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        if (this.credential.getSecretId() != null && (!this.credential.getSecretId().isEmpty())) {
            param.put("SecretId", this.credential.getSecretId());
        }
        if (this.credential.getToken() != null && (!this.credential.getToken().isEmpty())) {
            param.put("Token", this.credential.getToken());
        }
        if (null != this.profile.getLanguage()) {
            param.put("Language", this.profile.getLanguage().getValue());
        }

        String strParam = "";
        try {
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                strParam +=
                        (URLEncoder.encode(entry.getKey(), "utf-8")
                                + "="
                                + URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8")
                                + "&");
            }
            //tood 加密
//            strParam += ("Signature=" + URLEncoder.encode(sigOutParam, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new STException("", e);
        }
        return strParam;
    }


    protected <T> T processResponseSSE(Response resp, Class<T> typeOfT, STCircuitBreaker.Token breakerToken) throws STException {
        STSSEResponseModel responseModel;
        try {
            responseModel = (STSSEResponseModel) typeOfT.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new STException("", e);
        }
        responseModel.setRequestId(resp.header("X-TC-RequestId"));
        responseModel.setToken(breakerToken);
        responseModel.setResponse(resp);
        return (T) responseModel;
    }

    protected <T> T processResponseJson(Response resp, Class<T> typeOfT, STCircuitBreaker.Token breakerToken) throws STException {
        String body;
        try {
            body = resp.body().string();
        } catch (IOException e) {
            String msg = "Cannot transfer response body to string, because Content-Length is too large, or Content-Length and stream length disagree.";
            log.info(msg);
            throw new STException(msg, e);
        }

        STJsonResponseModel<STJsonResponseErrModel> errResp;
        try {
            Type errType = new TypeToken<STJsonResponseModel<STJsonResponseErrModel>>() {
            }.getType();
            errResp = gson.fromJson(body, errType);
        } catch (JsonSyntaxException e) {
            String msg = "json is not a valid representation for an object of type";
            log.info(msg);
            throw new STException(msg, e);
        }

        if (errResp.response.getError() != null) {
            if (breakerToken != null) {
                STJsonResponseErrModel error = errResp.response;
                boolean regionOk = error.getRequestId() != null
                        && !error.getRequestId().isEmpty()
                        && error.getError().getCode() != null
                        && !error.getError().getCode().equals("InternalError");
                breakerToken.report(regionOk);
            }
            throw new STException(
                    errResp.response.getError().getMessage(),
                    errResp.response.getRequestId(),
                    errResp.response.getError().getCode());
        }

        Type type = TypeToken.getParameterized(STJsonResponseModel.class, typeOfT).getType();
        return ((STJsonResponseModel<T>) gson.fromJson(body, type)).response;
    }

}
