package com.bank.service;

import com.bank.dto.TransactionDto;
import com.bank.dto.converter.Converter;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.repository.TransactionRepo;
import com.bank.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class TransactionService {

    private final static String SSN_NOT_FOUND_MSG = "account with ssn %s not found";
    private  final UserRepo userRepo;
    private  final TransactionRepo transactionRepo;
    private final Converter converter;

    public List<TransactionDto> getUserTransactionDto(HttpServletRequest request) throws ResourceNotFoundException{

        String ssn = (String) request.getAttribute("ssn");
        User user = userRepo.findBySsn(ssn).
                orElseThrow( () -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG, ssn)));

        List<Transaction> transactionList = user.getTransactions();

        List<TransactionDto> transactionDtoList = transactionList.stream().
                map(this.converter::transactionToTransactionDto).collect(Collectors.toList());

        return transactionDtoList;
    }

    public List<TransactionDto> getAllTransactions() throws ResourceNotFoundException{

        transactionRepo.findAll();

        List<Transaction> transactionList = transactionRepo.findAll();

        List<TransactionDto> transactionDtoList = transactionList.stream().
                map(this.converter::transactionToTransactionDto).collect(Collectors.toList());

        return transactionDtoList;
    }

}
