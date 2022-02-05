package com.bank.dto;

import com.bank.model.enumeration.UserRoleName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long userId;

    @Pattern(regexp = "[1-9]\\d{2}[- ]?\\d{2}[- ]?\\d{4}$", message = "Please enter valid ssn number")
    @NotNull(message = "please enter your ssn number")
    private String ssn;

    @Size(max=50)
    @NotNull(message = "please enter your first name")
    private String firstName;

    @Size(max=50)
    @NotNull(message = "please enter your last name")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, locale = "tr", pattern = "dd/MM/yyyy")
    @Past(message = "please check your birth date")
    @NotNull(message = "please enter your date of birth day/month/year")
    private LocalDate dob;

    @Email(message ="Please enter valid email" )
    @Size(min = 5, max = 150)
    @NotNull(message ="Please enter your email" )
    private String email;

    @Size(min = 3, max = 20, message ="Please enter min 4 characters" )
    @NotNull(message ="Please enter your user name" )
    private String username;

    @JsonIgnore
    private String password;

    private Set<UserRoleName> role;

    private String description;

    public UserDto(String ssn, String firstName, String lastName, LocalDate dob,
                   String email, String username,Set<UserRoleName> role  ) {
        this.ssn = ssn;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.email = email;
        this.username=username;
        this.role = role;
    }

}
