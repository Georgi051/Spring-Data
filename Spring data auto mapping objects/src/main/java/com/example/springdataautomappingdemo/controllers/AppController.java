package com.example.springdataautomappingdemo.controllers;

import com.example.springdataautomappingdemo.domain.dtos.AddGameDto;
import com.example.springdataautomappingdemo.domain.dtos.EditGameDto;
import com.example.springdataautomappingdemo.domain.dtos.LoginUserDto;
import com.example.springdataautomappingdemo.domain.dtos.RegisterUserDto;
import com.example.springdataautomappingdemo.domain.entities.Role;
import com.example.springdataautomappingdemo.services.GameService;
import com.example.springdataautomappingdemo.services.UserService;
import com.example.springdataautomappingdemo.utils.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import java.io.BufferedReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class AppController implements CommandLineRunner {
    private final BufferedReader bufferedReader;

    private final ValidationUtil validationUtil;
    private final UserService userService;
    private final GameService gameService;


    @Autowired
    public AppController(BufferedReader bufferedReader, ValidationUtil validationUtil, UserService userService, GameService gameService) {
        this.bufferedReader = bufferedReader;
        this.validationUtil = validationUtil;
        this.userService = userService;
        this.gameService = gameService;
    }


    @Override
    public void run(String... args) throws Exception {
        while (true) {
            System.out.println("Enter input:");
            String[] data = bufferedReader.readLine().split("\\|");
            switch (data[0]) {
                case "RegisterUser":
                    if (!data[2].equals(data[3])) {
                        System.out.println("Incorrect username / password");
                        continue;
                    }
                    RegisterUserDto registerUserDto = new RegisterUserDto(data[1], data[2], data[4]);

                    if (this.validationUtil.isValid(registerUserDto)) {
                        this.userService.registerUser(registerUserDto);
                    } else {
                        violationMassages(registerUserDto);
                    }
                    break;
                case "LoginUser":
                    LoginUserDto loginUserDto = new LoginUserDto(data[1], data[2]);
                    this.userService.loginUser(loginUserDto);
                    break;
                case "Logout":
                    this.userService.logout();
                    break;
                case "AddGame":
                    if (checkForLogIn()) continue;
                    if (checkForAdmin()) continue;

                    AddGameDto addGameDto = new AddGameDto(data[1], new BigDecimal(data[2]), Double.parseDouble(data[3]),
                            data[4], data[5], data[6], LocalDate.parse(data[7], DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                    if (this.validationUtil.isValid(addGameDto)) {
                        this.gameService.addGame(addGameDto);
                    } else {
                        violationMassages(addGameDto);
                    }
                    break;
                case "EditGame":
                    if (checkForLogIn()) continue;
                    if (checkForAdmin()) continue;

                    EditGameDto editGameDto = insertDataInEditGameDto(data);
                    if (this.validationUtil.isValid(editGameDto)) {
                        this.gameService.editGame(editGameDto);
                    } else {
                        violationMassages(editGameDto);
                    }
                    break;
                case "DeleteGame":
                    if (checkForLogIn()) continue;
                    if (checkForAdmin()) continue;

                    Long id = Long.parseLong(data[1]);
                    this.gameService.deleteGame(id);
                    break;
                case "AllGames":
                    this.gameService.getAllGames();
                    break;
                case "DetailGame":
                    this.gameService.getGameInfoByName(data[1]);
                    break;
                default:
                    System.out.println("Not found that input case!");
            }
        }
    }

    private boolean checkForAdmin() {
        if (!userService.checkForAdminOrUser().getRole().equals(Role.ADMIN)) {
            System.out.println("User don't have permission");
            return true;
        }
        return false;
    }

    private boolean checkForLogIn() {
        if (userService.checkForAdminOrUser() == null) {
            System.out.println("To perform this operation you must first log in!!!");
            return true;
        }
        return false;
    }

    private EditGameDto insertDataInEditGameDto(String[] data) {
        EditGameDto editGameDto = new EditGameDto();
        editGameDto.setId(Long.parseLong(data[1]));

        String[] split;
        int i = 2;
        while (i < data.length) {
            if (data[i].contains("title")) {
                split = data[i].split("=");
                editGameDto.setTitle(split[1]);
            } else if (data[i].contains("price")) {
                split = data[i].split("=");
                editGameDto.setPrice(new BigDecimal(split[1]));
            } else if (data[i].contains("size")) {
                split = data[i].split("=");
                editGameDto.setSize(Double.parseDouble(split[1]));
            } else if (data[i].contains("trailer")) {
                split = data[i].split("=");
                editGameDto.setTrailer(split[1]);
            } else if (data[i].contains("thumbnailURL")) {
                split = data[i].split("=");
                editGameDto.setImage(split[1]);
            } else if (data[i].contains("description")) {
                split = data[i].split("=");
                editGameDto.setDescription(split[1]);
            }
            i++;
        }
        return editGameDto;
    }

    private <T> void violationMassages(T Dto) {
        this.validationUtil.getViolation(Dto)
                .stream().map(ConstraintViolation::getMessage)
                .forEach(System.out::println);
    }
}
