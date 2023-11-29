package com.sometool.dto.request;

import lombok.Data;

@Data
public class QywxRequest {
    private String agentid;
    private String corpId;
    private String corpSecret;

    public QywxRequest() {
    }

    public QywxRequest(String agentid, String corpId, String corpSecret) {
        this.agentid = agentid;
        this.corpId = corpId;
        this.corpSecret = corpSecret;
    }
}
