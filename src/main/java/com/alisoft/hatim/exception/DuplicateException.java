package com.alisoft.hatim.exception;

public class DuplicateException extends HatimException {
    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String obj,
                              String value,
                              String attribute) {
        this(obj + " с " + attribute + "='" + value + "' уже существует");
    }

}
