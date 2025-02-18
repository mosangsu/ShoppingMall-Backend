package com.uniwear.backend.exception;

import lombok.Getter;

@Getter
public class ApiCallFailedException extends RuntimeException {

    private String code;
    private String message;
    private Object data;

    public ApiCallFailedException(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}