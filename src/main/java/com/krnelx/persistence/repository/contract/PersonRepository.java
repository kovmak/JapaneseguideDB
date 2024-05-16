package com.krnelx.persistence.repository.contract;

import com.krnelx.persistence.entity.Person;
import com.krnelx.persistence.repository.Repository;
import java.util.Set;
import java.util.UUID;

public interface PersonRepository extends Repository<Person>, OneToMany {

    Set<Person> findAllByToyId(UUID toyId);
}
