package com.example.xmlcardealer.services;

import com.example.xmlcardealer.domain.dtos.Query1.CustomersRootDto;
import com.example.xmlcardealer.domain.dtos.Query5.CustomerRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.CustomerSeedDto;
import com.example.xmlcardealer.domain.entities.Customer;

import java.util.List;

public interface CustomerService  {
    void seedCustomer(List<CustomerSeedDto> customerSeedDto);

    Customer getRandomCustomer();

    CustomersRootDto getAllByBirthDate();

    List<Customer> getAll();

    CustomerRootDto getAllByAtLeastOnePurchasedCar();
}
