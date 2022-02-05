package com.bank.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@Table(schema = "public")
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Please enter your date")
    @Column(nullable = false, name = "transaction_date")
    private Date date;

    @Size(min=3, max = 100)
    @NotNull(message = "please enter your description")
    @Column(name = "tansaction_description" ,nullable = true )
    private String description;

    @NotBlank(message = "Please enter your transactionType")
    @Column(name = "transaction_Type",nullable = false)
    private String type;

    @Positive(message = "Amount must be positive")
    @Column(nullable = false)
    private double amount;

    @Positive(message = "Balance must be positive")
    private BigDecimal availableBalance;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Transaction(Date date, String description, String type, double amount,
                       BigDecimal availableBalance, Account account, User user) {
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.account = account;
        this.user=user;
    }

}
