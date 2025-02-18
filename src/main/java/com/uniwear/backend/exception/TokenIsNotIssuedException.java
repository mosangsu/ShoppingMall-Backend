package com.uniwear.backend.exception;

import lombok.Getter;

@Getter
public class TokenIsNotIssuedException extends RuntimeException {

    private String code;
    private String message;

    public TokenIsNotIssuedException(String code, String message) {
        this.code = code;
        this.message = message;
    }
}