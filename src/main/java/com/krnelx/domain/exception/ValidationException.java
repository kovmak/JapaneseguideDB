package com.krnelx.domain.exception;

import jakarta.validation.ConstraintViolation;
import java.io.Serial;
import java.util.Set;

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3145152535586258949L;
    private final Set<? extends ConstraintViolation<?>> violations;

    public ValidationException(String message,
        Set<? extends ConstraintViolation<?>> violations) {
        super(message);
        this.violations = violations;
    }

    public Set<? extends ConstraintViolation<?>> getViolations() {
        return violations;
    }

    public static ValidationException create(String suffix,
        Set<? extends ConstraintViolation<?>> violations) {
        return new ValidationException("Помилка валідації при" + suffix, violations);
    }
}
