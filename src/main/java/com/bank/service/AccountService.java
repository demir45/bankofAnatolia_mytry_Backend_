package com.bank.service;

import com.bank.dto.AccountDto;
import com.bank.dto.converter.Converter;
import com.bank.exception.BadRequestException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transaction;
import com.bank.model.User;
import com.bank.model.enumeration.AccountStatusType;
import com.bank.model.enumeration.AccountType;
import com.bank.model.enumeration.TransactionType;
import com.bank.payload.request.TransferRequest;
import com.bank.repository.AccountRepo;
import com.bank.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class AccountService {

    private final static String SSN_NOT_FOUND_MSG = "account with ssn %s not found";

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    private final static String ACCOUNT_NOT_FOUND_MSG = "account with id %d not found";

    private final AccountRepo accountRepo;

    private final UserRepo userRepo;

    private final Converter converter;


    public Map<String, Boolean> createAccount(Account account) throws BadRequestException {


        System.out.println("AccountService / createAccount createDate : " + account.getCreateDate());
        System.out.println("AccountService / createAccount closedDate : " + account.getClosedDate());
        accountRepo.save(account);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Account created successfuly", true);

        return map;
    }

    public List<AccountDto> getAccountsBySsn(HttpServletRequest request) throws BadRequestException{

        String ssn = (String) request.getAttribute("ssn");

        User user = userRepo.findBySsn(ssn).
                orElseThrow( () -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG, ssn)));

        List<Account> userAccounts = user.getAccounts();
        List<AccountDto> accountDto = userAccounts.stream().
                                                   map(this.converter::accountToAccountDto).
                                                   collect(Collectors.toList());

        return accountDto;

    }

    public List<AccountDto> getAllAccountDtos() throws BadRequestException {

        AccountDto accountDto = new AccountDto();

        List<Account> accounts = accountRepo.findAll();

        List<AccountDto> accountDtoList = accounts.stream().
                                            map(this.converter::accountToAccountDto).
                                            collect(Collectors.toList());

        return accountDtoList;
    }

    public void assignAccount(Account account, User user, HttpServletRequest request)throws BadRequestException {

        String ssn = (String) request.getAttribute("ssn");
        String employee = userRepo.findBySsn(ssn).get().getFirstName() +" " +userRepo.findBySsn(ssn).get().getLastName();
        account.setUser(user);
        account.setEmployee(employee);
        accountRepo.save(account);
    }

    public Map<String, Boolean> deleteAccount(Long accountId) throws BadRequestException {

        accountRepo.deleteById(accountId);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Account deleted successfuly", true);

        return map;
    }

    public Map<String, Boolean> deposit(HttpServletRequest request, TransferRequest transferRequest) throws BadRequestException {

        String ssn = (String) request.getAttribute("ssn");
        String accountDescriptions = transferRequest.getAccountDescriptions();

        double amount =  transferRequest.getAmount();
        User user = userRepo.findBySsn(ssn).
                orElseThrow(
                        () -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG,ssn)));

        Account account = accountRepo.findByDescription(accountDescriptions).
                orElseThrow(
                        () -> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, accountDescriptions)));

        if(amount < 0){
            throw new BadRequestException("Amount should be greater than 0");
        }

        account.setAccountBalance(account.getAccountBalance().add(new BigDecimal(amount)));

        Date date = new Date();


        Transaction tran = new Transaction(
                                            date,
                                            accountDescriptions,
                                            TransactionType.DEPOSIT.toString(),
                                            amount,
                                            account.getAccountBalance(),
                                            account,
                                            user);

        List<Transaction> transactionList = account.getTransactions();
                          transactionList.add(tran);

        account.setTransactions(transactionList);

        accountRepo.save(account);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Amount successfully deposited", true);
        return map;
    }

    public Map<String, Boolean> withdraw(HttpServletRequest request, TransferRequest transferRequest) throws BadRequestException{

        String ssn = (String) request.getAttribute("ssn");
        String accountDescriptions = transferRequest.getAccountDescriptions();
        double amount = transferRequest.getAmount();

        User user = userRepo.findBySsn(ssn).
                orElseThrow(
                        () -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG,ssn)));

        Account account = accountRepo.findByDescription(accountDescriptions).
                orElseThrow(
                        () -> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, accountDescriptions)));

        if(amount > account.getAccountBalance().intValue()){
            throw new BadRequestException("Amount should be lower than balance");
        }else if(amount < 0){
            throw new BadRequestException("Amount should be greater than 0");
        }

        account.setAccountBalance(account.getAccountBalance().subtract(new BigDecimal(amount)));

        Date date = new Date();


        Transaction tran = new Transaction(
                date,
                accountDescriptions,
                TransactionType.WITHDRAW.toString(),
                amount,
                account.getAccountBalance(),
                account,
                user);

        List<Transaction> transactionList = account.getTransactions();
        transactionList.add(tran);

        account.setTransactions(transactionList);

        accountRepo.save(account);


        Map<String, Boolean> map = new HashMap<>();
        map.put("Amount successfully withdrawed", true);
        return map;

    }


    public Map<String, Boolean> accountToAccountTransfer(HttpServletRequest request, TransferRequest transferRequest) throws BadRequestException{

        String ssn = (String) request.getAttribute("ssn");
        User user = userRepo.findBySsn(ssn).
                orElseThrow( () -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG,ssn)));

        String fromDescription = transferRequest.getFromAccount();
        Account fromAccount = accountRepo.findByDescription(fromDescription).
                orElseThrow( () -> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, fromDescription)));

        String toDescription =  transferRequest.getToAccount();
        Account toAccount = accountRepo.findByDescription(toDescription).
                orElseThrow( () -> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, toDescription)));


        Double amount = transferRequest.getAmount();

        if(amount < 0){
            throw new BadRequestException("Amount should be greater than 0");
        }
        if(amount > fromAccount.getAccountBalance().doubleValue()){
            throw new BadRequestException("Amount shouldn't be greater than balance");
        }
        toAccount.setAccountBalance(toAccount.getAccountBalance().add(new BigDecimal(amount)));

        fromAccount.setAccountBalance(fromAccount.getAccountBalance().subtract(new BigDecimal(amount)));

        Date date = new Date();
///////---FROM ACCOUNT TRANSACTION
        Transaction from = new Transaction(
                date,
                fromAccount.getDescription(),
                TransactionType.TRANSFER_WITHDRAW.toString(),
                amount,
                fromAccount.getAccountBalance(),
                fromAccount,
                user);

        List<Transaction> transactionfromList = fromAccount.getTransactions();
        transactionfromList.add(from);

        fromAccount.setTransactions(transactionfromList);
///////---TO ACCOUNT TRANSACTION
        Transaction to = new Transaction(
                date,
                toAccount.getDescription(),
                TransactionType.TRANSFER_DEPOSIT.toString(),
                amount,
                toAccount.getAccountBalance(),
                toAccount,
                user);

        List<Transaction> transactionToList = toAccount.getTransactions();
        transactionToList.add(to);

        toAccount.setTransactions(transactionToList);


        accountRepo.save(toAccount);
        accountRepo.save(fromAccount);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Amount successfully transfered", true);
        return map;

    }

    public Map<String, Boolean> updateAccount(HttpServletRequest request, Map<String, Object> mapAccount) throws BadRequestException{


        String description = (String) mapAccount.get("description");

        Long userId = (long) ((Integer) mapAccount.get("userId"));

        Long accountId = (long)((Integer) mapAccount.get("accountId"));

        if(userId != -1) {
            User user = userRepo.findById(userId).
                    orElseThrow(() -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, userId)));
        }

        Account account = accountRepo.findById(accountId).
                orElseThrow( () -> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, accountId)));

        account.setDescription((String) mapAccount.get("description"));
        account.setAccountType(AccountType.valueOf((String) mapAccount.get("accountType")));
        account.setAccountStatusType(AccountStatusType.valueOf((String) mapAccount.get("accountStatusType")) );
        account.setClosedDate(Timestamp.valueOf((String) mapAccount.get("closedDate")));
        account.setEmployee((String) mapAccount.get("employee"));

        accountRepo.save(account);

        Map<String, Boolean> map = new HashMap<>();
        map.put("Account updated successfuly", true);

        return map;
    }


}
