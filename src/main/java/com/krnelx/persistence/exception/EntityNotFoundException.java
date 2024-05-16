package com.krnelx.persistence.exception;

import java.io.Serial;

public class EntityNotFoundException extends RuntimeException {

    @Serial private static final long serialVersionUID = -6486296592874470767L;

    public EntityNotFoundException(String message) {
        super(message);
    }
}