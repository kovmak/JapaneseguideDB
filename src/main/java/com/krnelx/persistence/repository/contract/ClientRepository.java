package com.krnelx.persistence.repository.contract;

import com.krnelx.persistence.entity.Client;
import com.krnelx.persistence.entity.Users.UsersRole;
import com.krnelx.persistence.repository.Repository;
import java.util.Optional;
import java.util.Set;

public interface ClientRepository extends Repository<Client> {
    Optional<Client> findByName(String name);
    Set<Client> findByUserRole(UsersRole UsersRole);
}
