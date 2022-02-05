package com.bank.controller;

import com.bank.model.User;
import com.bank.security.jwt.JwtUtils;
import com.bank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@CrossOrigin(origins = "https://bankofprl.netlify.app", allowedHeaders = "*")
@AllArgsConstructor
@RestController
public class LoginController {


    private final UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final HttpServletRequest request;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Boolean>> registerCustomer(@Valid @RequestBody User user){
        return new ResponseEntity<>(userService.register(user), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, Object> userMap){

        return new ResponseEntity<>(userService.login(userMap), HttpStatus.OK);

    }



}
