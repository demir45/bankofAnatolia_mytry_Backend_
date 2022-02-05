package com.bank.repository;

import com.bank.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepo extends CrudRepository<Transaction, Long> {

    Optional<Transaction> findById(Long id);
    List<Transaction> findAll();

}
