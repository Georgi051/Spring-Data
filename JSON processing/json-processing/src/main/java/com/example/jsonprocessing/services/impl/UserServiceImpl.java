package com.example.jsonprocessing.services.impl;

import com.example.jsonprocessing.models.dtos.seedData.UserSeedDto;
import com.example.jsonprocessing.models.dtos.fourQuery.ProductDto;
import com.example.jsonprocessing.models.dtos.fourQuery.ProductsDto;
import com.example.jsonprocessing.models.dtos.fourQuery.UserAndProductsCountsDto;
import com.example.jsonprocessing.models.dtos.fourQuery.UsersDto;
import com.example.jsonprocessing.models.entities.User;
import com.example.jsonprocessing.repositories.UserRepository;
import com.example.jsonprocessing.services.UserService;
import com.example.jsonprocessing.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedUsers(UserSeedDto[] userSeedDto) {
        if (this.userRepository.count() != 0) {
            return;
        }
        Arrays.stream(userSeedDto).forEach(uDto -> {
            if (this.validationUtil.isValid(uDto)) {
                if (checkForExisting(uDto)) {
                    System.out.printf("%s %s already exist%n", uDto.getFirstName(), uDto.getLastName());
                    return;
                }
                User user = this.modelMapper.map(uDto, User.class);
                this.userRepository.saveAndFlush(user);
            } else {
                this.validationUtil.getViolation(uDto)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
        addFriends();
    }

    private void addFriends() {
        List<User> all = this.userRepository.findAll();

        Random random = new Random();
        for (User user : all) {
            Set<User> friends = new HashSet<>();

            long numFriend = random.nextInt(((int) (this.userRepository.count())) + 1) % 5;
            if (numFriend == 0) {
                numFriend = 3;
            }
            for (int j = 0; j < numFriend; j++) {
                long id = random.nextInt((int) this.userRepository.count()) + 1;
                User randomUser = this.userRepository.findById(id).orElse(null);
                if (!(user.getLastName().equals(randomUser.getLastName()))) {
                    friends.add(randomUser);
                }
            }
            user.setFriends(friends);
            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public User getRandomUser() {
        Random random = new Random();
        long id = random.nextInt((int) (this.userRepository.count())) + 1;
        return userRepository.getOne(id);
    }

    @Override
    public long getAllUsers() {
        return this.userRepository.count();
    }

    @Override
    public UserAndProductsCountsDto getAllUserWithAtLeastOneProductSell() {
        List<User> allByProductSellsIsNotNull = this.userRepository.findAllByProductSellsIsNotNull();

        List<UsersDto> uDto = allByProductSellsIsNotNull.stream()
                .map(u -> {

                    ProductsDto productInfo = new ProductsDto();
                    u.getProductSells().stream().map(p -> {
                        List<ProductDto> countAndProducts = p.getSeller().getProductSells().stream()
                                .map(product -> {
                                    ProductDto oneProduct = this.modelMapper.map(product, ProductDto.class);
                                    return oneProduct;
                                }).collect(Collectors.toList());

                        productInfo.setCount(countAndProducts.size());
                        productInfo.setProducts(countAndProducts);

                        return countAndProducts;
                    }).collect(Collectors.toList());

                    UsersDto userInfo = this.modelMapper.map(u, UsersDto.class);
                    userInfo.setSoldProducts(productInfo);
                    return userInfo;
                }).collect(Collectors.toList());

        UserAndProductsCountsDto result = new UserAndProductsCountsDto();
        result.setUsersCount(uDto.size());
        result.setUsers(uDto);
        return result;
    }


    private boolean checkForExisting(UserSeedDto userSeedDto) {
        User user = this.userRepository.findFirstByFirstNameAndLastName(userSeedDto.getFirstName()
                , userSeedDto.getLastName());
        return user != null;
    }

}
