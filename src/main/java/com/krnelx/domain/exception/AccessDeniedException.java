package com.krnelx.domain.exception;

import java.io.Serial;

public class AccessDeniedException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 174948262083496647L;

    public AccessDeniedException(String message) {
        super(message);
    }

    public static AccessDeniedException notAuthorUser(String suffix) {
        return new AccessDeniedException(STR."Ви не є автором, тому не маєте права \{suffix}.");
    }

    public static AccessDeniedException notAuthorOrBannedUser(String suffix) {
        return new AccessDeniedException(
            STR."Ви не є автором або забанені, тому не маєте права \{suffix}.");
    }

    public static AccessDeniedException bannedUser(String suffix) {
        return new AccessDeniedException(STR."Ви забанені, тому не маєте права \{suffix}.");
    }
}
