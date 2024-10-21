package com.example.ecommerce.exception;

public class UserValidationException  extends RuntimeException  {
    private final String field;

    public UserValidationException(String message, String field) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
