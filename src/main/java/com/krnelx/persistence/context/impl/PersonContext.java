package com.krnelx.persistence.context.impl;

import com.krnelx.persistence.context.GenericUnitOfWork;
import com.krnelx.persistence.entity.Person;
import com.krnelx.persistence.repository.contract.PersonRepository;
import org.springframework.stereotype.Component;

@Component
public class PersonContext extends GenericUnitOfWork<Person> {

    public final PersonRepository repository;

    public PersonContext(PersonRepository repository){
        super(repository);
        this.repository = repository;
    }
}
