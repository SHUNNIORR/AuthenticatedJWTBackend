package com.unkownkoder;

import java.util.HashSet;
import java.util.Set;

import com.unkownkoder.models.Account;
import com.unkownkoder.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.unkownkoder.models.ApplicationUser;
import com.unkownkoder.models.Role;
import com.unkownkoder.repository.RoleRepository;
import com.unkownkoder.repository.UserRepository;

@SpringBootApplication
public class AuthenticatedBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(AuthenticatedBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncode, AccountRepository accountRepository){
		return args ->{
			if(roleRepository.findByAuthority("ADMIN").isPresent()) return;
			Role adminRole = roleRepository.save(new Role("ADMIN"));
			Role userRole = roleRepository.save(new Role("USER"));
			Set<Role> rolAdmin = new HashSet<>();
			rolAdmin.add(adminRole);

			Set<Role> rolUser = new HashSet<>();
			rolAdmin.add(userRole);

			ApplicationUser admin = new ApplicationUser(1, "admin", passwordEncode.encode("password"), rolAdmin);
			ApplicationUser user1 = new ApplicationUser(2,"jorge",  passwordEncode.encode("12345678"),rolUser);
			ApplicationUser user2 = new ApplicationUser(3,"ariana",  passwordEncode.encode("12345678"),rolUser);

			//create accounts for jorge and ariana

			Account accountAdmin = new Account(1L,"AHORROS",10000.00,admin);
			Account accountJorge = new Account(2L,"AHORROS",10000.00,user1);
			Account accountAriana = new Account(3L,"AHORROS",10000.00,user2);

			userRepository.save(admin);
			userRepository.save(user1);
			userRepository.save(user2);

			//accountRepository.save(accountAdmin);
			//accountRepository.save(accountJorge);
			//accountRepository.save(accountAriana);
		};
	}
}
