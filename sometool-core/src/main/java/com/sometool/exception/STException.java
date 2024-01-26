package com.sometool.exception;

import lombok.Data;

@Data
public class STException extends Exception {
    private static final long serialVersionUID = 1L;


    private String errorCode;
    private String message;

    public STException(String message) {
        this.message = message;
    }

    public STException(String message, Throwable cause) {
        super(message, cause);
    }


    public STException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    public String toString() {
        return "[STException]"
                + "code: "
                + this.getErrorCode()
                + " message:"
                + this.getMessage();
    }
}
