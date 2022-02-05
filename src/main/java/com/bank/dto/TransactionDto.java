package com.bank.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "tr", pattern = "dd/MM/yyyy HH:mm", timezone="Europe/Istanbul", lenient = OptBoolean.FALSE)
    @NotEmpty(message = "Please enter your date")
    private Date date;

    @Size(min=3, max = 100)
    @NotNull(message = "please enter your description")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotBlank(message = "Please enter your transactionType")
    private String type;

    @Positive(message = "Amount must be positive")
    private double amount;

    @Positive(message = "Balance must be positive")
    private BigDecimal availableBalance;

    private Long accountId;

    public TransactionDto(Long id, Date date, String description, String type,
                          double amount, BigDecimal availableBalance, Long accountId) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.availableBalance = availableBalance;
        this.accountId = accountId;
    }
}
