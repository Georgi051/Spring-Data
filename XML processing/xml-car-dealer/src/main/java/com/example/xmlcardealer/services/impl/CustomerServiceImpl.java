package com.example.xmlcardealer.services.impl;

import com.example.xmlcardealer.domain.dtos.Query1.CustomersDto;
import com.example.xmlcardealer.domain.dtos.Query1.CustomersRootDto;
import com.example.xmlcardealer.domain.dtos.Query5.CustomerDto;
import com.example.xmlcardealer.domain.dtos.Query5.CustomerRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.CustomerSeedDto;
import com.example.xmlcardealer.domain.dtos.seedData.CustomerSeedRootDto;
import com.example.xmlcardealer.domain.entities.Customer;
import com.example.xmlcardealer.domain.entities.Part;
import com.example.xmlcardealer.domain.entities.Sale;
import com.example.xmlcardealer.repositories.CustomerRepository;
import com.example.xmlcardealer.services.CustomerService;
import com.example.xmlcardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Random random;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Random random) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.random = random;
    }

    @Override
    public void seedCustomer(List<CustomerSeedDto> customerSeedDto) {
        if (this.customerRepository.count() != 0) {
            return;
        }

        customerSeedDto.forEach(c -> {
            if (this.validationUtil.isValid(c)) {

                Customer customer = this.modelMapper.map(c, Customer.class);
                String date = c.getBirthDate();
                LocalDateTime birthDate = parseDate(date);
                customer.setBirthDate(birthDate);
                this.customerRepository.saveAndFlush(customer);
            } else {
                this.validationUtil.violations(c)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    private LocalDateTime parseDate(String date) {
        return LocalDateTime.parse(date,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Override
    public Customer getRandomCustomer() {
        long randomId = this.random
                .nextInt((int) this.customerRepository.count()) + 1;
        return this.customerRepository.findById(randomId).orElse(null);
    }

    @Override
    public CustomersRootDto getAllByBirthDate() {
        List<Customer> allByOrderByBirthDateDesc = this.customerRepository.getAllByOrderByBirthDateAscYoungDriver();
        CustomersRootDto cDto = new CustomersRootDto();

        List<CustomersDto> customersDtoList = new ArrayList<>();
        allByOrderByBirthDateDesc.forEach(c -> {
            CustomersDto customersDto = this.modelMapper.map(c, CustomersDto.class);
            customersDtoList.add(customersDto);
        });
        cDto.setCustomers(customersDtoList);
        return cDto;
    }

    @Override
    public List<Customer> getAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public CustomerRootDto getAllByAtLeastOnePurchasedCar() {
        CustomerRootDto customersRootDto = new CustomerRootDto();
        List<CustomerDto> collect = this.customerRepository.findAll().stream()
                .filter(c -> c.getSales().size() >= 1)
                .map(customer -> {
                    CustomerDto customerDto = new CustomerDto();
                    customerDto.setFullName(customer.getName());
                    customerDto.setBoughtCars(customer.getSales().size());

                    BigDecimal totalMoney = new BigDecimal("0");
                    for (Sale sale : customer.getSales()) {
                        for (Part part : sale.getCar().getParts()) {
                            BigDecimal onePartPrice = part.getPrice();
                            totalMoney = totalMoney.add(onePartPrice);
                        }
                        BigDecimal discount = totalMoney.multiply(BigDecimal.valueOf(sale.getDiscount()));
                        totalMoney = totalMoney.subtract(discount);
                    }
                    customerDto.setSpentMoney(totalMoney);
                    return customerDto;
                }).collect(Collectors.toList());

        List<CustomerDto> filterData = collect.stream().sorted((f, s) -> {
            int result = s.getSpentMoney().compareTo(f.getSpentMoney());
            if (result == 0) {
                result = s.getBoughtCars().compareTo(f.getBoughtCars());
            }
            return result;
        }).collect(Collectors.toList());

        customersRootDto.setCustomerDtoList(filterData);
        return customersRootDto;
    }
}
