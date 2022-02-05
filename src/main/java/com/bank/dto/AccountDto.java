package com.bank.dto;

import com.bank.model.enumeration.AccountStatusType;
import com.bank.model.enumeration.AccountType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class AccountDto {

    private Long id;

    @NotBlank(message = "Please enter your description")
    @Size(min =3)
    private String description;

    private BigDecimal accountBalance;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Please enter your accountType")
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Please enter your accountStatusType")
    private AccountStatusType accountStatusType;

    private Date  createDate;

    private Date closedDate;

    private String employee;
    private Long userId;

    private List<TransactionDto> transactions;


    public AccountDto(String description, BigDecimal accountBalance,
                      AccountType accountType, AccountStatusType accountStatusType,
                      Date createDate, Date  closedDate, String employee, Long userId ,List<TransactionDto> transactions) {
        this.description = description;
        this.accountBalance = accountBalance;
        this.accountType = accountType;
        this.accountStatusType = accountStatusType;
        this.createDate = createDate;
        this.closedDate = closedDate;
        this.employee = employee;
        this.transactions=transactions;

    }
}
