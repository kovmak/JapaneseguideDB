package com.krnelx.persistence.context.factory;


import com.krnelx.persistence.context.impl.*;
import com.krnelx.persistence.context.impl.EventsContext;
import org.springframework.stereotype.Component;

@Component
public class PersistenceContext {

    public final PersonContext persons;
    public final ClientContext clients;
    public final EventsContext events;
    public final DescriptionContext description;
    public final UserContext users;

    public PersistenceContext(
        PersonContext personContext,
        ClientContext clientContext,
        EventsContext eventsContext,
        DescriptionContext descriptionContext,
        UserContext userContext) {
        this.persons = personContext;
        this.clients = clientContext;
        this.events = eventsContext;
        this.description = descriptionContext;
        this.users = userContext;
    }
}