package com.krnelx.persistence.context.impl;

import com.krnelx.persistence.context.GenericUnitOfWork;
import com.krnelx.persistence.entity.Users;
import com.krnelx.persistence.repository.contract.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserContext extends GenericUnitOfWork<Users> {

    public final UserRepository repository;

    protected UserContext(UserRepository repository) {
        super(repository);
        this.repository = repository;
    }
}
