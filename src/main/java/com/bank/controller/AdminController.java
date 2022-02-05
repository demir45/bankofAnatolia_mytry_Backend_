package com.bank.controller;

import com.bank.dto.AccountDto;
import com.bank.dto.UserDto;
import com.bank.service.AccountService;
import com.bank.service.UserService;
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
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AdminController {

    private final UserService userService;

    private final AccountService accountService;

    @GetMapping("/allusers")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<UserDto>> getAllUsers(HttpServletRequest request){

        return new ResponseEntity<>(userService.getAllUsers(request), HttpStatus.OK);
    }

    @GetMapping("/allAccounts")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<AccountDto>> getAllAccounts(){
        return new ResponseEntity<>(accountService.getAllAccountDtos(), HttpStatus.OK);
    }

    @PatchMapping("/updateSingleUserInfo")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> updateSingleUserInfo(HttpServletRequest request,
                                                                     @Valid @RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.updateSingleUserInfo(request,userDto), HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public ResponseEntity<Map<String, Boolean>> deleteUser(@PathVariable Long id){

        return new ResponseEntity<>(userService.deleteUserById(id), HttpStatus.OK);
    }
}
