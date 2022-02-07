package com.bank;

import com.bank.model.Role;
import com.bank.model.User;
import com.bank.model.UserRole;
import com.bank.model.enumeration.UserRoleName;
import com.bank.repository.RoleRepo;
import com.bank.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

	 @Autowired
	UserRepo userRepo;


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

		Optional<User> userADmin = userRepo.findBySsn("123-45-6789");
		if(!userADmin.isPresent()){

			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			User user = new User();
			UserRole userRole = new UserRole();

			user.setSsn("123-45-6789");
			user.setFirstName("John");
			user.setLastName("Wick");
			user.setEmail("wick@gmail.com");
			user.setPassword("12345");
			user.setUsername("johnwick");
			user.setDob(LocalDate.of(1995, 12, 15));

			userRole.setUser(user);
			userRole.setRole(admin.get());
			userRepo.save(user);
		}



	}
}