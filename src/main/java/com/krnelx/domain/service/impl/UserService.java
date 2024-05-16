package com.krnelx.domain.service.impl;

import com.krnelx.domain.dto.UserGudesDto;
import com.krnelx.domain.exception.ValidationException;
import com.krnelx.persistence.context.factory.PersistenceContext;
import com.krnelx.persistence.context.impl.UserContext;
import com.krnelx.persistence.entity.Users;
import com.krnelx.persistence.entity.Users.UsersRole;
import jakarta.validation.Validator;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserContext userContext;
    private final Validator validator;

    public UserService(PersistenceContext persistenceContext, Validator validator) {
        this.userContext = persistenceContext.users;
        this.validator = validator;
    }


    public Users create(UserGudesDto userStoreDto) {
        var violations = validator.validate(userStoreDto);
        if (!violations.isEmpty()) {
            throw ValidationException.create("Збереженні користувача", violations);
        }

        Users user = new Users(
            userStoreDto.id(),
            userStoreDto.login(),
            userStoreDto.password(),
            Objects.nonNull(userStoreDto.role()) ? userStoreDto.role() : UsersRole.CLIENT,
            userStoreDto.name()
        );

        userContext.registerNew(user);
        userContext.commit();
        return null;
    }
}