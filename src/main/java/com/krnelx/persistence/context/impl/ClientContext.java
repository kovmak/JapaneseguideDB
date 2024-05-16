package com.krnelx.persistence.context.impl;

import com.krnelx.persistence.context.GenericUnitOfWork;
import com.krnelx.persistence.entity.Client;
import com.krnelx.persistence.repository.contract.ClientRepository;
import org.springframework.stereotype.Component;

@Component
public class ClientContext extends GenericUnitOfWork<Client> {

    public final ClientRepository repository;

    public ClientContext(ClientRepository repository){
        super(repository);
        this.repository = repository;
    }
}