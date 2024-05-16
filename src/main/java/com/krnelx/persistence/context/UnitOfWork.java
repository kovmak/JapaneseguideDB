package com.krnelx.persistence.context;

import com.krnelx.persistence.entity.Entity;
import java.util.Set;
import java.util.UUID;

public interface UnitOfWork<T extends Entity> {

    String INSERT = "INSERT";
    String DELETE = "DELETE";
    String MODIFY = "MODIFY";

    void registerNew(T entity);

    void registerModified(T entity);

    void registerDeleted(T entity);
    void registerDeleted(UUID id);

    void commit();

    T getEntity(UUID id);

    T getEntity();

    Set<T> getEntities();
}
