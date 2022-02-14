package com.bank.controller;


import com.bank.dto.UserDto;
import com.bank.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@AllArgsConstructor
@CrossOrigin(origins = "https://bankofanatoliaa.netlify.app", allowedHeaders = "*")
@RestController
@RequestMapping()
public class UserController {


    private final UserService userService;

    @GetMapping("/getUserBySsn")
    public ResponseEntity<UserDto> getUserBySsn(HttpServletRequest request){

        return new ResponseEntity<>(userService.findBySsn(request), HttpStatus.OK);
    }

    @PatchMapping("/user/updateUserInfo")
    public ResponseEntity<Map<String, Boolean>> updateUserInfo(HttpServletRequest request,
                                                                @RequestBody Map<String, Object> userMap){
        return  ResponseEntity.ok(userService.updateUser(request, userMap));
    }

    @PatchMapping("/user/updatePassword")
    public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
                                                               @RequestBody Map<String, Object> mapPassword){
        return new ResponseEntity<>(userService.updatePassword(request, mapPassword), HttpStatus.OK);
    }

}
