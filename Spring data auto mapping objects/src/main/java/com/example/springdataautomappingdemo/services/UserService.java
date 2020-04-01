package com.example.springdataautomappingdemo.services;


import com.example.springdataautomappingdemo.domain.dtos.LoginUserDto;
import com.example.springdataautomappingdemo.domain.dtos.RegisterUserDto;
import com.example.springdataautomappingdemo.domain.dtos.UserDto;

public interface UserService {
    void registerUser(RegisterUserDto registerUserDto);

    void loginUser(LoginUserDto loginUserDto);

    void logout();

    UserDto checkForAdminOrUser();


}
