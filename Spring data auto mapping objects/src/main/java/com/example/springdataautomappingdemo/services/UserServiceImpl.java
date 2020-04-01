package com.example.springdataautomappingdemo.services;

import com.example.springdataautomappingdemo.domain.dtos.LoginUserDto;
import com.example.springdataautomappingdemo.domain.dtos.RegisterUserDto;
import com.example.springdataautomappingdemo.domain.dtos.UserDto;
import com.example.springdataautomappingdemo.domain.entities.Role;
import com.example.springdataautomappingdemo.domain.entities.User;
import com.example.springdataautomappingdemo.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private UserDto userDto;




    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public void registerUser(RegisterUserDto registerUserDto) {
        User user = this.modelMapper.map(registerUserDto, User.class);
        user.setRole(this.userRepository.count() == 0 ? Role.ADMIN : Role.USER);
        if (userRepository.findByEmail(user.getEmail()) != null) {
            System.out.println("This email already exist!");
            return;
        }
        System.out.printf("%s was registered%n", user.getFullName());
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void loginUser(LoginUserDto loginUserDto) {
        User user = this.userRepository.findByEmail(loginUserDto.getEmail());
        if (user == null) {
            System.out.println("Incorrect username / password");
        } else {
            this.userDto = this.modelMapper.map(user, UserDto.class);
            System.out.printf("Successfully logged in %s%n", user.getFullName());
        }
    }

    @Override
    public void logout() {
        if (userDto != null) {
            System.out.printf("User %s successfully logged out%n", userDto.getFullName());
            this.userDto = null;
        } else {
            System.out.println("Cannot log out. No user was logged in.");
        }
    }


    @Override
    public UserDto checkForAdminOrUser() {
        return this.userDto;
    }
}
