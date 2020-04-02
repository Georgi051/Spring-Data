package com.example.xmlcardealer.controllers;


import com.example.xmlcardealer.domain.dtos.Query1.CustomersRootDto;
import com.example.xmlcardealer.domain.dtos.Query2.ToyotaRootDto;
import com.example.xmlcardealer.domain.dtos.Query3.LocalSuppliersRootDto;
import com.example.xmlcardealer.domain.dtos.Query4.CarViewDto;
import com.example.xmlcardealer.domain.dtos.Query5.CustomerRootDto;
import com.example.xmlcardealer.domain.dtos.Query6.SaleRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.*;
import com.example.xmlcardealer.services.*;
import com.example.xmlcardealer.utils.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import static com.example.xmlcardealer.constants.GlobalConstants.*;


@Component
public class AppController implements CommandLineRunner {
    private SupplierService supplierService;
    private PartService partService;
    private CarService carService;
    private CustomerService customerService;
    private SaleService saleService;
    private BufferedReader bufferedReader;
    private XmlParser xmlParser;

    @Autowired
    public AppController(SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, SaleService saleService, BufferedReader bufferedReader, XmlParser xmlParser) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.saleService = saleService;
        this.bufferedReader = bufferedReader;
        this.xmlParser = xmlParser;
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
                case "6":
                    getAllSalesWithDiscount();
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

    private void getAllSalesWithDiscount() throws JAXBException {
        SaleRootDto saleRootDto = this.saleService.getAllSellsWithDiscount();
        this.xmlParser.marshalToFile(SIXTH_QUERY,saleRootDto);
    }

    private void getAllCustomerWithAtLeastOneCar() throws JAXBException {
        CustomerRootDto customersDtos = this.customerService.getAllByAtLeastOnePurchasedCar();
        this.xmlParser.marshalToFile(FIFTH_QUERY,customersDtos);
    }

    private void getAllCarsWithTheirListOfParts() throws JAXBException {
        CarViewDto carDtos = this.carService.getAllCarsWithTheirParts();
        this.xmlParser.marshalToFile(FOURTH_QUERY,carDtos);
    }

    private void getLocalSuppliers() throws JAXBException {
        LocalSuppliersRootDto localSuppliersDtos = this.supplierService.getAllLocalSuppliers(false);
        this.xmlParser.marshalToFile(THIRD_QUERY,localSuppliersDtos);

    }

    private void getAllToyotaModels() throws JAXBException {
        ToyotaRootDto toyotaCarsDtos = this.carService.getAllToyotaMake("Toyota");
        this.xmlParser.marshalToFile(SECOND_QUERY,toyotaCarsDtos);
    }

    private void getAllCustomersOrderByBirthDate() throws JAXBException {
        CustomersRootDto customersDtos = this.customerService.getAllByBirthDate();
        this.xmlParser.marshalToFile(FIRST_QUERY,customersDtos);
    }

    private void seedSells() {
        this.saleService.seedSale();
    }

    private void seedCustomer() throws FileNotFoundException, JAXBException {
        CustomerSeedRootDto seedDto = this.xmlParser.unmarshalFromFile(CUSTOMERS_FILE_PATH,CustomerSeedRootDto.class);
        this.customerService.seedCustomer(seedDto.getCustomers());
    }

    private void seedCars() throws FileNotFoundException, JAXBException {
        CarSeedRootDto seedDto = this.xmlParser.unmarshalFromFile(CARS_FILE_PATH, CarSeedRootDto.class);
        this.carService.seedCar(seedDto.getCars());
    }

    private void seedParts() throws FileNotFoundException, JAXBException {
       PartSeedRootDto seedDto = this.xmlParser.unmarshalFromFile(PARTS_FILE_PATH,PartSeedRootDto.class);
        this.partService.seedParts(seedDto.getParts());
    }

    private void seedSuppliers() throws FileNotFoundException, JAXBException {
        SupplierRootSeedDto seedDto = this.xmlParser.unmarshalFromFile(SUPPLIERS_FILE_PATH, SupplierRootSeedDto.class);
        this.supplierService.seedSupplier(seedDto.getSuppliers());
    }

    private void startProgram() {
        System.out.printf("%n\t\t\t\t\t\t\t\t\t\t\t\t\t%s%n%n",INFO);
        System.out.println("To seed data into base first press 0 else press query number:");
    }
}
