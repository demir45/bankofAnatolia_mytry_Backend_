package com.bank;

import com.bank.model.Role;
import com.bank.model.enumeration.UserRoleName;
import com.bank.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.Optional;

@SpringBootApplication
public class BankofPrlMytryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankofPrlMytryApplication.class, args);
	}

}

@Component
class DemoCommandLineRunner implements CommandLineRunner{

	 @Autowired
	 RoleRepo roleRepo;


	@Override
	public void run(String... args) throws Exception {

		Optional<Role> admin =  roleRepo.findByName(UserRoleName.ROLE_ADMIN);
		Optional<Role> employee =  roleRepo.findByName(UserRoleName.ROLE_EMPLOYEE);
		Optional<Role> customer =  roleRepo.findByName(UserRoleName.ROLE_CUSTOMER);

		if(!admin.isPresent()){
			Role roleAdmin = new Role();
			roleAdmin.setName(UserRoleName.ROLE_ADMIN);
			roleRepo.save(roleAdmin);
		}
		if(!employee.isPresent()){
			Role roleEmployee = new Role();
			roleEmployee.setName(UserRoleName.ROLE_EMPLOYEE);
			roleRepo.save(roleEmployee);
		}
		if(!customer.isPresent()){
			Role roleCustomer = new Role();
			roleCustomer.setName(UserRoleName.ROLE_CUSTOMER);
			roleRepo.save(roleCustomer);
		}




	}
}