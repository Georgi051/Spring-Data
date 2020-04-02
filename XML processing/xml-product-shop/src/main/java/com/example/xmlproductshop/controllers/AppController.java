package com.example.xmlproductshop.controllers;

import com.example.xmlproductshop.models.dtos.Query1.ProductsRootDto;
import com.example.xmlproductshop.models.dtos.Query2.UsersPrimeDto;
import com.example.xmlproductshop.models.dtos.Query3.CategoriesRootDto;
import com.example.xmlproductshop.models.dtos.Query4.UsersRootDto;
import com.example.xmlproductshop.models.dtos.seedData.CategoriesSeedRootDto;
import com.example.xmlproductshop.models.dtos.seedData.ProductsSeedRootDto;
import com.example.xmlproductshop.models.dtos.seedData.UsersSeedRootDto;
import com.example.xmlproductshop.servises.CategoryService;
import com.example.xmlproductshop.servises.ProductService;
import com.example.xmlproductshop.servises.impl.UserServiceImpl;
import com.example.xmlproductshop.utils.XmlParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import static com.example.xmlproductshop.constants.GlobalConstants.*;

@Component
public class AppController implements CommandLineRunner {
    private UserServiceImpl userService;
    private CategoryService categoryService;
    private ProductService productService;
    private XmlParser xmlParser;
    private BufferedReader bufferedReader;

    public AppController(UserServiceImpl userService, CategoryService categoryService,
                         ProductService productService, XmlParser xmlParser, BufferedReader bufferedReader) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.xmlParser = xmlParser;
        this.bufferedReader = bufferedReader;
    }


    @Override
    public void run(String... args) throws Exception {

        System.out.println("To seed data into base first press 0 else press query number:");
        String queryTaskNum = bufferedReader.readLine();
        while (true) {
            switch (queryTaskNum) {
                case "0":
                    seedUsers();
                    seedCategories();
                    seedProducts();
                    break;
                case "1":
                    productsInRange();
                    break;
                case "2":
                    successfullySoldProducts();
                    break;
                case "3":
                    getAllCategoriesWithProducts();
                    break;
                case "4":
                    getAllUsersWithAtLeastOneSoldProduct();
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

    private void getAllUsersWithAtLeastOneSoldProduct() throws JAXBException {
        UsersRootDto uDto = this.userService.getAllUsersAndProducts();
        this.xmlParser.marshalToFile(USERS_AND_PRODUCTS, uDto);
    }

    private void getAllCategoriesWithProducts() throws JAXBException {
        CategoriesRootDto cDto = this.categoryService.getAllCategoriesByProductCount();
        this.xmlParser.marshalToFile(CATEGORIES_BY_PRODUCTS_COUNT, cDto);
    }

    private void successfullySoldProducts() throws JAXBException {
        UsersPrimeDto pDto = this.userService.getAllUsersAtLeastOneSoldProduct();
        this.xmlParser.marshalToFile(SUCCESSFULLY_SOLD_PRODUCTS, pDto);
    }

    private void productsInRange() throws JAXBException {
        ProductsRootDto pDto = this.productService.getAllProductsBetween();
        this.xmlParser.marshalToFile(PRODUCTS_IN_RANGE, pDto);
    }

    private void seedCategories() throws JAXBException, FileNotFoundException {
        CategoriesSeedRootDto categorySeedDtos =
                this.xmlParser.unmarshalFromFile(CATEGORIES_SEED, CategoriesSeedRootDto.class);
        this.categoryService.seedCategory(categorySeedDtos.getCategoryList());
    }

    private void seedProducts() throws JAXBException, FileNotFoundException {
        ProductsSeedRootDto productSeedDto = this.xmlParser.unmarshalFromFile(PRODUCTS_SEED, ProductsSeedRootDto.class);
        this.productService.seedProduct(productSeedDto.getProducts());
    }

    private void seedUsers() throws JAXBException, FileNotFoundException {
        UsersSeedRootDto userSeedDto = this.xmlParser.unmarshalFromFile(USERS_SEED, UsersSeedRootDto.class);
        this.userService.seedUser(userSeedDto.getUsers());
    }
}
