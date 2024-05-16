package com.krnelx.persistence.repository.contract;

import com.krnelx.persistence.entity.Users;
import com.krnelx.persistence.repository.Repository;
import java.util.Optional;

public interface UserRepository extends Repository<Users> {

    Optional<Users> findByLogin(String login);

    Optional<Users> findByName(String name);
}
