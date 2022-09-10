package org.khasanof.redisserver.data.repository;

import org.khasanof.redisserver.data.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, String> {
    Role findFirstByName(String name);
}
