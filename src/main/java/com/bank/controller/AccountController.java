package com.bank.controller;

import com.bank.dto.AccountDto;
import com.bank.dto.TransactionDto;
import com.bank.model.Account;
import com.bank.payload.request.TransferRequest;
import com.bank.service.AccountService;
import com.bank.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin(origins = "https://bankofprl.netlify.app", allowedHeaders = "*")
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    private final TransactionService transactionService;

    @PostMapping("/createAccount")
    public ResponseEntity<Map<String, Boolean>> createAccount(@Valid @RequestBody Account account){

        return new ResponseEntity<>(accountService.createAccount(account), HttpStatus.CREATED );
    }

    @GetMapping("/getUserAccounts")
    public ResponseEntity<List<AccountDto>> getUserAccounts(HttpServletRequest request){

        return new ResponseEntity<>(accountService.getAccountsBySsn(request), HttpStatus.OK);
    }

    @PatchMapping("/deposit")
    public ResponseEntity<Map<String, Boolean>> deposit(HttpServletRequest request,
                                                        @RequestBody TransferRequest transferRequest){

        return new ResponseEntity<>(accountService.deposit(request,transferRequest), HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Map<String, Boolean>> withdraw(HttpServletRequest request,
                                                        @RequestBody TransferRequest transferRequest){

        return new ResponseEntity<>(accountService.withdraw(request,transferRequest), HttpStatus.OK);
    }

    @GetMapping("/userTransactions")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(HttpServletRequest request){

        return new ResponseEntity<>(transactionService.getUserTransactionDto(request), HttpStatus.OK);
    }

    @GetMapping("/allTransactions")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<TransactionDto>> getAllTransactions(){

        return new ResponseEntity<>(transactionService.getAllTransactions(), HttpStatus.OK);
    }


    @PostMapping("/moneyTransfer")
    public ResponseEntity<Map<String, Boolean>> accountToAccountTransfer(HttpServletRequest request,
                                                                         @RequestBody TransferRequest transferRequest){
        return new ResponseEntity<>(accountService.accountToAccountTransfer(request, transferRequest), HttpStatus.OK);
    }

    @PatchMapping("/updateAccount")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> updateAccount(HttpServletRequest request,
                                                              @RequestBody Map<String, Object> mapAccount){
        return new ResponseEntity<>(accountService.updateAccount(request, mapAccount), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{accountId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> deleteAccount(@PathVariable Long accountId){

        return new ResponseEntity<>(accountService.deleteAccount(accountId), HttpStatus.OK);
    }
}
