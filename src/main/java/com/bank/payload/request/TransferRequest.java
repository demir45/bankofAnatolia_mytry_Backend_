package com.bank.payload.request;

import lombok.Data;

@Data
public class TransferRequest {

    private String fromAccount;

    private String toAccount;

    private Double amount;

    private String explanation;

    private String accountDescriptions;

    private String comment;

}
