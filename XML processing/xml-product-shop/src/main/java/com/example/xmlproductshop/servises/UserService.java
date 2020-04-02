package com.example.xmlproductshop.servises;

import com.example.xmlproductshop.models.dtos.Query2.UsersPrimeDto;
import com.example.xmlproductshop.models.dtos.Query4.UsersRootDto;
import com.example.xmlproductshop.models.dtos.seedData.UserSeedDto;
import com.example.xmlproductshop.models.entities.User;

import java.util.List;

public interface UserService {
    void seedUser(List<UserSeedDto> usersSeedDtos);

    User getRandomUser();

    UsersPrimeDto getAllUsersAtLeastOneSoldProduct();

    UsersRootDto getAllUsersAndProducts();
}
