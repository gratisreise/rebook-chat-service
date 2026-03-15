package com.example.rebookchatservice.global.exception;

public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        super();
    }

    public UnAuthorizedException(String message) {
        super(message);
    }
}
