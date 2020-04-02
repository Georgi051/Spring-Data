package com.example.cardealer.controller;

import com.example.cardealer.domain.dtos.Query1.CustomersDto;
import com.example.cardealer.domain.dtos.Query2.ToyotaCarsDto;
import com.example.cardealer.domain.dtos.Query3.LocalSuppliersDto;
import com.example.cardealer.domain.dtos.Query4.CarViewDto;
import com.example.cardealer.domain.dtos.Query5.CustomerDto;
import com.example.cardealer.domain.dtos.seedData.CarSeedDto;
import com.example.cardealer.domain.dtos.seedData.CustomerSeedDto;
import com.example.cardealer.domain.dtos.seedData.PartSeedDto;
import com.example.cardealer.domain.dtos.seedData.SupplierSeedDto;
import com.example.cardealer.services.*;
import com.example.cardealer.utils.FileUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static com.example.cardealer.constants.GlobalConstants.*;

@Component
public class AppController implements CommandLineRunner {
    private SupplierService supplierService;
    private PartService partService;
    private CarService carService;
    private CustomerService customerService;
    private SaleService saleService;
    private Gson gson;
    private FileUtil fileUtil;
    private BufferedReader bufferedReader;

    @Autowired
    public AppController(SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, SaleService saleService, Gson gson, FileUtil fileUtil, BufferedReader bufferedReader) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.gson = gson;
        this.fileUtil = fileUtil;
        this.bufferedReader = bufferedReader;
    }

    @Override
    public void run(String... args) throws Exception {
        startProgram();
        String queryTaskNum = bufferedReader.readLine();
        while (true) {
            switch (queryTaskNum) {
                case "0":
                    seedSuppliers();
                    seedParts();
                    seedCars();
                    seedCustomer();
                    seedSells();
                    break;
                case "1":
                    getAllCustomersOrderByBirthDate();
                    break;
                case "2":
                    getAllToyotaModels();
                    break;
                case "3":
                    getLocalSuppliers();
                    break;
                case "4":
                    getAllCarsWithTheirListOfParts();
                    break;
                case "5":
                    getAllCustomerWithAtLeastOneCar();
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



    private void getAllCustomerWithAtLeastOneCar() throws IOException {
        List<CustomerDto> customersDtos = this.customerService.getAllByAtLeastOnePurchasedCar();
        String json = this.gson.toJson(customersDtos);
        this.fileUtil.write(json,FIFTH_QUERY);
    }

    private void getAllCarsWithTheirListOfParts() throws IOException {
        List<CarViewDto> carDtos = this.carService.getAllCarsWithTheirParts();
        String json = this.gson.toJson(carDtos);
        this.fileUtil.write(json,FOURTH_QUERY);
    }

    private void getLocalSuppliers() throws IOException {
        List<LocalSuppliersDto> localSuppliersDtos = this.supplierService.getAllLocalSuppliers(false);
        String json = this.gson.toJson(localSuppliersDtos);
        this.fileUtil.write(json,THIRD_QUERY);
    }

    private void getAllToyotaModels() throws IOException {
        List<ToyotaCarsDto> toyotaCarsDtos = this.carService.getAllToyotaMake("Toyota");
        String json = this.gson.toJson(toyotaCarsDtos);
        this.fileUtil.write(json,SECOND_QUERY);
    }

    private void getAllCustomersOrderByBirthDate() throws IOException {
        List<CustomersDto> customersDtos = this.customerService.getAllByBirthDate();
        String json = this.gson.toJson(customersDtos);
        this.fileUtil.write(json,FIRST_QUERY);
    }

    private void seedSells() {
        this.saleService.seedSale();
    }

    private void seedCustomer() throws FileNotFoundException {
        CustomerSeedDto[] seedDto = this.gson.fromJson(new FileReader(CUSTOMERS_FILE_PATH),CustomerSeedDto[].class);
        this.customerService.seedCustomer(seedDto);
    }

    private void seedCars() throws FileNotFoundException {
        CarSeedDto[] seedDto = this.gson.fromJson(new FileReader(CARS_FILE_PATH),CarSeedDto[].class);
        this.carService.seedCar(seedDto);
    }

    private void seedParts() throws FileNotFoundException {
        PartSeedDto[] seedDto = this.gson.fromJson(new FileReader(PARTS_FILE_PATH),PartSeedDto[].class);
        this.partService.seedParts(seedDto);
    }

    private void seedSuppliers() throws FileNotFoundException {
        SupplierSeedDto[] seedDto = this.gson.fromJson(new FileReader(SUPPLIERS_FILE_PATH),SupplierSeedDto[].class);
        this.supplierService.seedSupplier(seedDto);
    }

    private void startProgram() {
        System.out.printf("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t%s%n","<<< WELLCOME >>>");
        System.out.printf("%n\t\t\t\t\t\t\t\t\t\t\t\t\t%s%n%n",INFO);
        System.out.println("To seed data into base first press 0 else press query number:");
    }
}
