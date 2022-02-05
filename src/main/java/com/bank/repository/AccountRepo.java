package com.bank.repository;

import com.bank.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

    Optional<Account> findById(Long id);
    Optional<Account> findByDescription(String description);
    Boolean existsByDescription(String description);
    Boolean existsByAccountType(String accountType);
}
