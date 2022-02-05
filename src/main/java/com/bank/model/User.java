package com.bank.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.*;

import com.bank.model.Account;
import com.bank.model.UserRole;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "USER_SEQUENCE_NAME", sequenceName = "USER_SEQUENCE_NAME", initialValue =3500, allocationSize = 1)
@Table(name = "users", schema = "public")
public class User implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy =GenerationType.SEQUENCE, generator = "USER_SEQUENCE_NAME" )
	private Long userId;
	
	@Pattern(regexp = "[1-9]\\d{2}[- ]?\\d{2}[- ]?\\d{4}$", message = "Please enter valid ssn number")
	@NotNull(message = "please enter your ssn number")
	@Column(nullable = false ,unique = true)
	private String ssn;
	
	@Size(max=50)
	@NotNull(message = "please enter your first name")
	@Column(nullable = false, length = 50)
	private String firstName;
	
	@Size(max=50)
	@NotNull(message = "please enter your last name")
	@Column(nullable = false, length = 50)
	private String lastName;

	//@JsonDeserialize(using = LocalDateDeserializer.class, as = LocalDate.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, locale = "tr", pattern = "dd/MM/yyyy")
	@Past(message = "please check your birth date")
	@NotNull(message = "please enter your date of birth day/month/year")
	@Column(name="date_of_birth" ,nullable = false)
	private LocalDate dob;

	@Email(message ="Please enter valid email" )
	@Size(min = 5, max = 150)
	@NotNull(message ="Please enter your email" )
	@Column(nullable = false, unique = true, length =150 )
	private String email;
	
	@Size(min = 3, max = 20, message ="Please enter min 4 characters" )
	@NotNull(message ="Please enter your user name" )
	@Column(nullable = false, unique = true, updatable = false, length = 20)
	private String username;
	
	@Size(min = 4, max = 60, message ="Please enter min 4 characters" )
	@NotNull(message ="Please enter your password" )
	@Column(nullable = false, length = 120)
	private String password;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Transaction> transactions;

	public User(
			String ssn, String firstName, String lastName, LocalDate dob,
			String email, String username, String password, Set<UserRole> userRoles) {
		
		this.ssn = ssn;
		this.firstName = firstName;
		this.lastName = lastName;
		this.dob = dob;
		this.email = email;
		this.username = username;
		this.password = password;
		this.userRoles = userRoles;
	}

}
