package com.bank.repository;

import com.bank.model.UserRole;
import org.springframework.data.repository.CrudRepository;

public interface UserRoleRepo extends CrudRepository<UserRole, Long > {
}
