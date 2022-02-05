package com.bank.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.*;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.bank.model.User;
import com.bank.model.enumeration.AccountStatusType;
import com.bank.model.enumeration.AccountType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "public")
@Entity
public class Account implements Serializable{
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

	@NotBlank(message = "Please enter your description")
	@Size(min =3)
	@Column(nullable = false)
    private String description;
    
	@Column(nullable = true)	
    private BigDecimal accountBalance;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please enter your accountType")
    @Column(nullable = false)
    private AccountType accountType;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please enter your accountStatusType")
    @Column(nullable = false)
    private AccountStatusType accountStatusType;

    @JsonFormat(shape = Shape.STRING, locale = "tr", pattern = "dd/MM/yyyy HH:mm", timezone="Europe/Istanbul", lenient = OptBoolean.FALSE)
    //@FutureOrPresent(message = "please check your date")// Kaydı güncellediğimizde createDate geçmişte kalırsa hata oluşturuyor.
    @NotNull(message = "Please enter your date")
    @Column(nullable = false)
    private Date createDate;
    
 //   @JsonSerialize(as = Date.class)
    @JsonFormat(shape = Shape.STRING, locale = "tr", pattern = "dd/MM/yyyy HH:mm", timezone="Europe/Istanbul", lenient = OptBoolean.FALSE)
    //@Future(message = "please check your date")
    @NotNull(message = "Please enter your date")
    @Column(nullable = true)
    private Timestamp closedDate;

    @Column(nullable = true)
    private String employee;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy="account", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transaction> transactions;

	public Account(String description, BigDecimal accountBalance, AccountType accountType, AccountStatusType accountStatusType,
                   Date  createDate, Timestamp  closedDate, String employee) {
		
		this.description = description;
		this.accountBalance = accountBalance;
		this.accountType = accountType;
		this.accountStatusType = accountStatusType;
		this.createDate = createDate;
		this.closedDate = closedDate;
		this.employee = employee;
	}
    
    
    
    
    
    
    
    
    
    
    
}
