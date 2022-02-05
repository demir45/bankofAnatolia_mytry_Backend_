package com.bank.dto.converter;

import com.bank.dto.AccountDto;
import com.bank.dto.TransactionDto;
import com.bank.dto.UserDto;
import com.bank.model.*;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@NoArgsConstructor
@Component
public class Converter {


    public UserDto userConverter(User user){

        UserDto userDto = new UserDto();

        userDto.setSsn(user.getSsn());

        return null;
    }
    public AccountDto accountToAccountDto(Account account){

        AccountDto accountDto = new AccountDto();

        accountDto.setId(account.getId());
        accountDto.setDescription(account.getDescription());
        accountDto.setAccountBalance(account.getAccountBalance());
        accountDto.setAccountType(account.getAccountType());
        accountDto.setAccountStatusType(account.getAccountStatusType());
        accountDto.setCreateDate(account.getCreateDate());
        accountDto.setClosedDate(account.getClosedDate());
        accountDto.setEmployee(account.getEmployee());
        try{
            accountDto.setUserId(account.getUser().getUserId());
        }catch(Exception e){
            accountDto.setUserId((long) -1);
        }

        accountDto.setTransactions(account.getTransactions().
                                    stream().map(this::transactionToTransactionDto).
                                    collect(Collectors.toList()));


        return accountDto;
    }
    public  TransactionDto transactionToTransactionDto(Transaction transaction){

        TransactionDto transactionDto = new TransactionDto(
                                        transaction.getId(),
                                        transaction.getDate(),
                                        transaction.getDescription(),
                                        transaction.getType().toString(),
                                        transaction.getAmount(),
                                        transaction.getAvailableBalance(),
                                        transaction.getAccount().getId() );
        return transactionDto;
    }

    public UserDto userToUserDto(User user){

        UserDto userDto = new UserDto();
        userDto.setSsn(user.getSsn());
        userDto.setUserId(user.getUserId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setDob(user.getDob());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getUserRoles().stream().
                map( userRoleName -> userRoleName.getRole().getName()).
                collect(Collectors.toSet()));


        return userDto;
    }

    public  User updateSingleUserdtoForAdmin(UserDto userDto, User user){

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setDob(userDto.getDob());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());

        return user;
    }


}
