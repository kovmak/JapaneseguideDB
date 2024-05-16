package com.krnelx.persistence.repository;

import com.krnelx.persistence.entity.Entity;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface Repository<T extends Entity> {

    Optional<T> findById(UUID id);

    Optional<T> findBy(String column, Object value);

    Optional<T> findByLogin(String column, Object value);
    Set<T> findAll();
    Set<T> findAllWhere(String whereQuery);

    T save(T entity);

    Set<T> save(Collection<T> entities);

    boolean delete(UUID id);

    boolean delete(Collection<UUID> ids);
}
