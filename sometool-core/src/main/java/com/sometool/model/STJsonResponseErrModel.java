package com.sometool.model;

import lombok.Data;

@Data
public class STJsonResponseErrModel {

    private String requestId;


    private ErrorInfo error;

    @Data
    public class ErrorInfo {
        private String code;

        private String message;
    }
}
