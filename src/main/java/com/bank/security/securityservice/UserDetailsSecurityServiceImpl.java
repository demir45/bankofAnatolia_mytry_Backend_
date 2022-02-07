package com.bank.security.securityservice;

import com.bank.model.User;
import com.bank.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@AllArgsConstructor
@Service
public class UserDetailsSecurityServiceImpl implements UserDetailsService {

   // @Autowired
   private final UserRepo userRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String ssn) throws UsernameNotFoundException {

       User user = userRepo.findBySsn(ssn)
               .orElseThrow(() -> new UsernameNotFoundException("User Not Found with ssn: " + ssn));

        return UserDetailsImpl.build(user);
    }
}
