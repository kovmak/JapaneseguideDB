package com.krnelx.persistence.repository.contract;

import com.krnelx.persistence.entity.Events;
import com.krnelx.persistence.repository.Repository;
import java.util.Optional;

public interface EventsRepository extends Repository<Events>, OneToMany {

    Optional<Events> findByName(String name);

    Optional<Events> findByDescription(String description);
}
