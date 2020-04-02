package com.example.jsonprocessing.controller;

import com.example.jsonprocessing.models.dtos.firstQuery.ProductInRangeDto;
import com.example.jsonprocessing.models.dtos.secondQuery.SellerWithLeastOneProductDto;
import com.example.jsonprocessing.models.dtos.fourQuery.UserAndProductsCountsDto;
import com.example.jsonprocessing.models.dtos.seedData.CategorySeedDto;
import com.example.jsonprocessing.models.dtos.seedData.ProductSeedDto;
import com.example.jsonprocessing.models.dtos.seedData.UserSeedDto;
import com.example.jsonprocessing.models.dtos.thirdQuery.CategoryByCountingDto;
import com.example.jsonprocessing.services.CategoryService;
import com.example.jsonprocessing.services.ProductService;
import com.example.jsonprocessing.services.UserService;
import com.example.jsonprocessing.utils.FileUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.example.jsonprocessing.constants.GlobalConstants.*;

@Component
public class AppController implements CommandLineRunner {
    private Gson gson;
    private CategoryService categoryService;
    private ProductService productService;
    private UserService userService;
    private FileUtil fileUtil;
    private BufferedReader bufferedReader;

    @Autowired
    public AppController(Gson gson, CategoryService categoryService, ProductService productService, UserService userService, FileUtil fileUtil, BufferedReader bufferedReader) {
        this.gson = gson;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userService = userService;
        this.fileUtil = fileUtil;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {

        System.out.println("To seed data into base first press 0 else press query number:");
        String queryTaskNum = bufferedReader.readLine();
        while (true) {
            switch (queryTaskNum) {
                case "0":
                    seedCategories();
                    seedUsers();
                    seedProducts();
                    break;
                case "1":
                    productInRange();
                    break;
                case "2":
                    userAtLestOneSoldItem();
                    break;
                case "3":
                    categoriesByProductsCount();
                    break;
                case "4":
                    usersAndProducts();
                    break;
                default:
                    System.out.println("Wrong query number:");
            }
            System.out.println("If you wanna EXIT type (end) else type number for different query:");

            queryTaskNum = bufferedReader.readLine();
            if (queryTaskNum.equals("end")) {
                System.out.println("Thank you and have a nice day");
                return;
            }
        }
    }

    private void usersAndProducts() throws IOException {
        UserAndProductsCountsDto uAndPCountDto = this.userService.getAllUserWithAtLeastOneProductSell();
        String json = this.gson.toJson(uAndPCountDto);
        this.fileUtil.write(json, USERS_AND_PRODUCTS);
    }

    private void categoriesByProductsCount() throws IOException {
        List<CategoryByCountingDto> allCategoryByName = this.categoryService.getAllCategoryByCount();
        String json = this.gson.toJson(allCategoryByName);
        this.fileUtil.write(json, CATEGORIES_BY_PRODUCTS_COUNT);
    }

    private void userAtLestOneSoldItem() throws IOException {
        List<SellerWithLeastOneProductDto> allProductWithBuyer = this.productService.getAllProductWithBuyer();
        String json = this.gson.toJson(allProductWithBuyer);
        this.fileUtil.write(json, SUCCESSFULLY_SOLD_PRODUCTS);
    }

    private void productInRange() throws IOException {
        List<ProductInRangeDto> productInRangeDto = this.productService.getProductInRange();
        String json = this.gson.toJson(productInRangeDto);
        this.fileUtil.write(json, CATEGORIES_BY_PRODUCT_COUNT);
    }

    private void seedUsers() throws FileNotFoundException {
        UserSeedDto[] userSeedDtos
                = this.gson.fromJson(new FileReader(USERS_FILE_PATH), UserSeedDto[].class);
        this.userService.seedUsers(userSeedDtos);
    }

    private void seedProducts() throws FileNotFoundException {
        ProductSeedDto[] productSeedDtos
                = this.gson.fromJson(new FileReader(PRODUCTS_FILE_PATH), ProductSeedDto[].class);
        this.productService.seedProducts(productSeedDtos);
    }

    private void seedCategories() throws FileNotFoundException {
        CategorySeedDto[] dtos
                = this.gson.fromJson(new FileReader(CATEGORIES_FILE_PATH), CategorySeedDto[].class);
        this.categoryService.seedCategory(dtos);
    }
}
