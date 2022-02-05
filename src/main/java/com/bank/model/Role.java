package com.bank.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.bank.model.enumeration.UserRoleName;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles", schema = "public")
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role_name", length = 30)
	private UserRoleName name;
	
	@OneToMany(mappedBy = "role",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private  Set<UserRole> userRoles = new HashSet<>();



		
	
	

}
