package com.krnelx.domain.exception;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Не вірний логін чи пароль.");
    }
}
