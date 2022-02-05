package com.bank;

import com.bank.model.Role;
import com.bank.model.enumeration.UserRoleName;
import com.bank.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

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
		Role roleAdmin = new Role();
		roleAdmin.setName(UserRoleName.ROLE_ADMIN);
		roleRepo.save(roleAdmin);

		Role roleEmployee = new Role();
		roleAdmin.setName(UserRoleName.ROLE_EMPLOYEE);
		roleRepo.save(roleEmployee);

		Role roleCustomer = new Role();
		roleAdmin.setName(UserRoleName.ROLE_CUSTOMER);
		roleRepo.save(roleCustomer);
	}
}