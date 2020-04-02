package com.example.cardealer.services.impl;

import com.example.cardealer.domain.dtos.Query1.CustomersDto;
import com.example.cardealer.domain.dtos.Query1.SaleDto;
import com.example.cardealer.domain.dtos.Query5.CustomerDto;
import com.example.cardealer.domain.dtos.seedData.CustomerSeedDto;
import com.example.cardealer.domain.entities.Customer;
import com.example.cardealer.domain.entities.Part;
import com.example.cardealer.domain.entities.Sale;
import com.example.cardealer.reopositories.CustomerRepository;
import com.example.cardealer.services.CustomerService;
import com.example.cardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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
    public void seedCustomer(CustomerSeedDto[] customerSeedDto) {
        if (this.customerRepository.count() != 0){
            return;
        }

        Arrays.stream(customerSeedDto).forEach(c -> {
            if (this.validationUtil.isValid(c)){

                    Customer customer = this.modelMapper.map(c,Customer.class);
                    String date = c.getBirthDate();
                    LocalDateTime birthDate = parseDate(date);
                    customer.setBirthDate(birthDate);
                    this.customerRepository.saveAndFlush(customer);
            }else {
                this.validationUtil.getViolation(c)
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
    public List<CustomersDto> getAllByBirthDate() {
        List<Customer> allByOrderByBirthDateDesc = this.customerRepository.getAllByOrderByBirthDateAscYoungDriver();

        List<CustomersDto> collect = allByOrderByBirthDateDesc.stream().map(c -> {
            CustomersDto customersDto = this.modelMapper.map(c, CustomersDto.class);
            List<SaleDto> sale = new ArrayList<>();
            List<Sale> sales = c.getSales();

            sales.forEach(s -> {
                SaleDto sDto = new SaleDto();
                sDto.setCar(String.format("%s %s",s.getCar().getMake(),s.getCar().getModel()));
                sDto.setTravelledDistance(s.getCar().getTravelledDistance());
                sDto.setDiscount(s.getDiscount());
                sale.add(sDto);
            });

            customersDto.setSales(sale);
            return customersDto;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Customer> getAll() {
        return this.customerRepository.findAll();
    }

    @Override
    public List<CustomerDto> getAllByAtLeastOnePurchasedCar() {
        List<CustomerDto> collect = this.customerRepository.findAll().stream().filter(c -> c.getSales().size() >= 1)
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

        return filterData;
    }

}
