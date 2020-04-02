package com.example.jsonprocessing.services;

import com.example.jsonprocessing.models.dtos.seedData.UserSeedDto;
import com.example.jsonprocessing.models.dtos.fourQuery.UserAndProductsCountsDto;
import com.example.jsonprocessing.models.entities.User;


public interface UserService {
    void seedUsers(UserSeedDto[] userSeedDto);

    User getRandomUser();

    long getAllUsers();

    UserAndProductsCountsDto getAllUserWithAtLeastOneProductSell();
}
