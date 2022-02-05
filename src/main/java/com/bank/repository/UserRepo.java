package com.bank.repository;


import com.bank.exception.ConflictException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepo extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username) throws ResourceNotFoundException;
    Optional<User> findBySsn(String ssn) throws ResourceNotFoundException;
    Optional<User> findByEmail(String email) throws ResourceNotFoundException;
    Boolean existsByUsername(String username) throws ResourceNotFoundException;
    Boolean existsByEmail(String email) throws ConflictException;
    Boolean existsBySsn(String ssn) throws ConflictException;
    //  id, ssn, first, last, dob, email, username, pass
    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.firstName=?2, u.lastName=?3, u.email=?4 WHERE u.ssn=?1")
    void update(String ssn, String firstName,
                String lastName, String email);

}
