package com.example.cardealer.services;

import com.example.cardealer.domain.dtos.Query1.CustomersDto;
import com.example.cardealer.domain.dtos.Query5.CustomerDto;
import com.example.cardealer.domain.dtos.seedData.CustomerSeedDto;
import com.example.cardealer.domain.entities.Customer;

import java.util.List;

public interface CustomerService  {
    void seedCustomer(CustomerSeedDto[] customerSeedDto);

    Customer getRandomCustomer();

    List<CustomersDto> getAllByBirthDate();

    List<Customer> getAll();

    List<CustomerDto> getAllByAtLeastOnePurchasedCar();
}
