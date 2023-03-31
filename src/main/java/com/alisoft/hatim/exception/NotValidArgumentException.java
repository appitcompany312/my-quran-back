package com.alisoft.hatim.exception;

import lombok.Getter;

@Getter
public class NotValidArgumentException extends HatimException {
    private String field;

    public NotValidArgumentException(String message) {
        super(message);
    }

    public NotValidArgumentException(String field, String message) {
        super(message);

        this.field = field;
    }

}
