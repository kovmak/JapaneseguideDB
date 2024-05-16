package com.krnelx.persistence.context.impl;

import com.krnelx.persistence.context.GenericUnitOfWork;
import com.krnelx.persistence.entity.Events;
import com.krnelx.persistence.repository.contract.EventsRepository;
import org.springframework.stereotype.Component;

@Component
public class EventsContext extends GenericUnitOfWork<Events> {

    public final EventsRepository repository;

    public EventsContext(EventsRepository repository){
        super(repository);
        this.repository = repository;
    }
}
