package com.krnelx.persistence.context.impl;

import com.krnelx.persistence.context.GenericUnitOfWork;
import com.krnelx.persistence.entity.Description;
import com.krnelx.persistence.repository.contract.DescriptionRepository;
import org.springframework.stereotype.Component;

@Component
public class DescriptionContext extends GenericUnitOfWork<Description> {

    public final DescriptionRepository repository;

    protected DescriptionContext(DescriptionRepository repository){
        super(repository);
        this.repository = repository;
    }
}
