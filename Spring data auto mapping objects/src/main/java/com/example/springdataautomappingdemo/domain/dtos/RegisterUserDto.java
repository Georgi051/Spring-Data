package com.example.springdataautomappingdemo.domain.dtos;

import org.springframework.lang.NonNull;

import javax.validation.constraints.Pattern;

public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;

    public RegisterUserDto(String email, String password, String fullName) {
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }

    public RegisterUserDto() {
    }

    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z]+\\.([a-z]){3,4}",message = "Incorrect email.")
    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{6,}$",message = "Incorrect username / password")
    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NonNull
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
