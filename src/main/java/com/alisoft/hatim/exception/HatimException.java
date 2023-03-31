package com.alisoft.hatim.exception;

import lombok.Getter;

@Getter
public class HatimException extends Exception {
    private final String message;

    public HatimException(String message) {
        this.message = message;
    }
}
