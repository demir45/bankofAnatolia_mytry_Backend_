package com.bank.service;

import com.bank.dto.UserDto;
import com.bank.dto.converter.Converter;
import com.bank.exception.AuthException;
import com.bank.exception.BadRequestException;
import com.bank.exception.ConflictException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.model.*;
import com.bank.model.enumeration.UserRoleName;
import com.bank.repository.AccountRepo;
import com.bank.repository.RoleRepo;
import com.bank.repository.UserRepo;
import com.bank.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService  {

    private final static String USER_NOT_FOUND_MSG = "user with id %d not found";

    private final static String SSN_NOT_FOUND_MSG = "user with ssn %s not found";

    private final static String ACCOUNT_NOT_FOUND_MSG = "account with id %s not found";

    private final UserRepo userRepo;

    private final RoleRepo roleRepo;

    private final AccountRepo accountRepo;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final AccountService accountService;

    private final Converter converter;


    public Map<String, Boolean> register(User user) throws BadRequestException {

        if (userRepo.existsBySsn(user.getSsn())){
            throw new ConflictException("Error: SSN is already in use!");
        }

        if (userRepo.existsByEmail(user.getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        if (userRepo.existsByUsername(user.getUsername())){
            throw  new ConflictException("Error: Username is already in use!");
        }

        // password encode ettik
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // yeni kişiye customer rolü vermek için
        Set<UserRole> roles = new HashSet<>();

        Role role = roleRepo.findByName(UserRoleName.ROLE_CUSTOMER)
                .orElseThrow(() -> new ResourceNotFoundException("Error: Role is not found."));

        roles.add(new UserRole(user, role));
        user.setUserRoles(roles);

        userRepo.save(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return map;

    }

    public Map<String, Object> login(Map<String, Object> userMap) throws AuthException {

        Map<String, Object> map = new HashMap<>();

        String ssn = (String) userMap.get("ssn");

        String password = (String) userMap.get("password");

        try {
            Optional<User> user = userRepo.findBySsn(ssn);

            // check password
            if (!BCrypt.checkpw(password, user.get().getPassword()))
                throw new AuthException("invalid credentials");
        } catch (Exception e) {

            throw new AuthException("invalid credentials");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(ssn, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

            map.put("token", jwt);

        return map;
    }

    public UserDto findBySsn(HttpServletRequest request) throws ResourceNotFoundException {

        String ssn = (String) request.getAttribute("ssn");

        User user = userRepo.findBySsn(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG, ssn)));

        Set<UserRoleName> role = user.getUserRoles().
                                                     stream().
                                                     map(roleName -> roleName.getRole().getName()).
                                                     collect(Collectors.toSet());

        return new UserDto(ssn,user.getFirstName(), user.getLastName(),
                user.getDob(), user.getEmail(),user.getUsername(), role);
    }

    public List<UserDto> getAllUsers(HttpServletRequest request) throws ResourceNotFoundException {

        String ssn = (String) request.getAttribute("ssn");
        User admin = userRepo.findBySsn(ssn).orElseThrow( () ->
                new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG, ssn)));

        List<UserRoleName> rolesAdmin =admin.getUserRoles().stream().
                map(userRoleName -> userRoleName.getRole().getName()).
                collect(Collectors.toList());

            List<User> userList = (List<User>) userRepo.findAll();

            List<UserDto> userDtoList = userList.stream().map(converter::userToUserDto).collect(Collectors.toList());

            return userDtoList;
    }


    public Map<String, Boolean> updateUser(HttpServletRequest request,
                                           Map<String, Object> userMap) throws BadRequestException {

        String ssn = (String) request.getAttribute("ssn");

        boolean emailExists = userRepo.existsByEmail((String) userMap.get("email"));

        User user = userRepo.findBySsn(ssn)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(SSN_NOT_FOUND_MSG,ssn)) );
        // check email
        if (emailExists && !userMap.get("email").equals(user.getEmail())){
            throw new ConflictException("Error: Email is already in use!");
        }

        user.setFirstName((String) userMap.get("firstName"));
        user.setLastName((String) userMap.get("lastName"));
        user.setEmail((String) userMap.get("email"));

        userRepo.save(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return map;
    }

    public Map<String, Boolean> deleteUserById(Long id) throws ResourceNotFoundException{

        // check user
        if(!userRepo.existsById(id))
            throw new ResourceNotFoundException("user does not exist");

        userRepo.deleteById(id);

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return map;
    }

    public Map<String, Boolean> updatePassword(HttpServletRequest request,
                                               Map<String, Object> mapPassword) throws BadRequestException{

        String ssn = (String) request.getAttribute("ssn");

        String oldPassword = (String) mapPassword.get("oldPassword");
        String newPassword = (String) mapPassword.get("newPassword");

        Optional<User> user = userRepo.findBySsn(ssn);

        // check password
        if(!(BCrypt.hashpw(oldPassword, user.get().getPassword()).equals(user.get().getPassword())))

            throw new BadRequestException("password does not match");

        String hashedPassword = passwordEncoder.encode(newPassword);

        user.get().setPassword(hashedPassword);

        userRepo.save(user.get());

        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);

        return map;
    }

    public Map<String, Boolean> updateSingleUserInfo(HttpServletRequest request,
                                                     UserDto userDto) throws BadRequestException{

        String updateSsn = userDto.getSsn();
        User user = userRepo.findBySsn(updateSsn).
                orElseThrow( () -> new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG,updateSsn)));

            converter.updateSingleUserdtoForAdmin(userDto, user);

        userDto.getRole().// add method of Set<>
                forEach(t-> user.getUserRoles().add( new UserRole(user, roleRepo.findByName(t).get())));

        if(userDto.getDescription() != null){
            Account account=accountRepo.findByDescription(userDto.getDescription()).
                    orElseThrow(()-> new ResourceNotFoundException(String.format(ACCOUNT_NOT_FOUND_MSG, userDto.getDescription())));
            accountService.assignAccount(account, user, request);
        }

        userRepo.save(user);

        Map<String, Boolean> map = new HashMap<>();
        map.put("User registered successfully!", true);
        return map;
    }

}
