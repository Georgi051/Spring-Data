package com.example.xmlproductshop.servises.impl;

import com.example.xmlproductshop.models.dtos.Query2.ProductInfoDto;
import com.example.xmlproductshop.models.dtos.Query2.SoldProductsRootDto;
import com.example.xmlproductshop.models.dtos.Query2.UserWithProductsDto;
import com.example.xmlproductshop.models.dtos.Query2.UsersPrimeDto;
import com.example.xmlproductshop.models.dtos.Query4.ProductViewDto;
import com.example.xmlproductshop.models.dtos.Query4.SoldProductDto;
import com.example.xmlproductshop.models.dtos.Query4.UserDataDto;
import com.example.xmlproductshop.models.dtos.Query4.UsersRootDto;
import com.example.xmlproductshop.models.dtos.seedData.UserSeedDto;
import com.example.xmlproductshop.models.entities.User;
import com.example.xmlproductshop.repositories.UserRepository;
import com.example.xmlproductshop.servises.UserService;
import com.example.xmlproductshop.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private ValidationUtil validationUtil;
    private Random random;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Random random) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedUser(List<UserSeedDto> usersSeedDtos) {
        if (this.userRepository.count() != 0) {
            return;
        }

        usersSeedDtos.forEach(userSeedDto -> {
            if (this.validationUtil.isValid(userSeedDto)) {
                if (this.userRepository.findByFirstNameAndLastNameAndAge(userSeedDto.getFirstName()
                        , userSeedDto.getLastName(), userSeedDto.getAge()) == null) {
                    User user = this.modelMapper.map(userSeedDto, User.class);

                    this.userRepository.saveAndFlush(user);
                } else {
                    System.out.printf("User %s %s %d already exist in data base!%n"
                            , userSeedDto.getLastName(), userSeedDto.getLastName(), userSeedDto.getAge());
                }
            } else {
                this.validationUtil.violations(userSeedDto).stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public User getRandomUser() {
        long id = this.random.nextInt((int) this.userRepository.count()) + 1;
        return this.userRepository.getOne(id);
    }

    @Override
    public UsersPrimeDto getAllUsersAtLeastOneSoldProduct() {

        List<User> users = this.userRepository.findAllByProductBuysGreaterThanOrderBy()
                .stream()
                .filter(user -> {
                    return user.getProductSells().stream()
                            .filter(product -> product.getBuyer() != null)
                            .count() > 0;

                }).collect(Collectors.toList());

        UsersPrimeDto usersPrimeDto = new UsersPrimeDto();
        List<UserWithProductsDto> usersDto = new ArrayList<>();
        users.forEach(user -> {
            List<SoldProductsRootDto> soldProducts = new ArrayList<>();
            List<ProductInfoDto> products = new ArrayList<>();

            user.getProductSells().forEach(product -> {
                if (product.getBuyer() != null) {
                    ProductInfoDto pDto = this.modelMapper.map(product, ProductInfoDto.class);
                    pDto.setFirstName(product.getBuyer().getFirstName());
                    pDto.setLastName(product.getBuyer().getLastName());
                    products.add(pDto);
                }
            });

            SoldProductsRootDto productInfoDto = new SoldProductsRootDto();

            productInfoDto.setProductsList(products);
            soldProducts.add(productInfoDto);
            UserWithProductsDto uDto = new UserWithProductsDto();
            uDto.setFirstName(user.getFirstName());
            uDto.setLastName(user.getLastName());
            uDto.setSoldProducts(soldProducts);
            usersDto.add(uDto);
        });
        usersPrimeDto.setUsers(usersDto);

        return usersPrimeDto;
    }

    @Override
    public UsersRootDto getAllUsersAndProducts() {

        List<UserDataDto> usersData = new ArrayList<>();
        UsersRootDto uDto = new UsersRootDto();

        this.userRepository.findAllByProductBuysGreaterThanOrderBy().forEach(user -> {
            UserDataDto userDataDto = this.modelMapper.map(user, UserDataDto.class);

            List<ProductViewDto> productsListInfo = new ArrayList<>();

            SoldProductDto soldProduct = new SoldProductDto();
            user.getProductSells().forEach(product -> {
                ProductViewDto sDto = this.modelMapper.map(product, ProductViewDto.class);
                productsListInfo.add(sDto);
            });
            soldProduct.setProductsInfo(productsListInfo);
            soldProduct.setCount(user.getProductSells().size());
            userDataDto.setSoldProductDto(soldProduct);

            usersData.add(userDataDto);
        });

        uDto.setCount(this.userRepository.findAllByProductBuysGreaterThanOrderBy().size());
        uDto.setUserInfo(usersData);

        return uDto;
    }
}
