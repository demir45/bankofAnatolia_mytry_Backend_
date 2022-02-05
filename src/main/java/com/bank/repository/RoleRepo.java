package com.bank.repository;

import com.bank.model.Role;
import com.bank.model.enumeration.UserRoleName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepo extends CrudRepository<Role, Long> {

    Optional<Role> findByName(UserRoleName name);
    List<Role> findAll();

}
