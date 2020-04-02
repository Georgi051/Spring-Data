package com.example.xmlcardealer.services.impl;

import com.example.xmlcardealer.domain.dtos.Query6.CarInfoDto;
import com.example.xmlcardealer.domain.dtos.Query6.CustomerInfoDto;
import com.example.xmlcardealer.domain.dtos.Query6.SaleRootDto;
import com.example.xmlcardealer.domain.entities.Customer;
import com.example.xmlcardealer.domain.entities.Part;
import com.example.xmlcardealer.domain.entities.Sale;
import com.example.xmlcardealer.repositories.SaleRepository;
import com.example.xmlcardealer.services.CarService;
import com.example.xmlcardealer.services.CustomerService;
import com.example.xmlcardealer.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


@Service
public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final CarService carService;
    private final CustomerService customerService;

    @Autowired
    public SaleServiceImpl(SaleRepository saleRepository, CarService carService, CustomerService customerService) {
        this.saleRepository = saleRepository;
        this.carService = carService;
        this.customerService = customerService;
    }

    @Override
    public void seedSale() {
        if (this.saleRepository.count() != 0){
            return;
        }

        for (int i = 1; i <= this.customerService.getAll().size(); i++) {
            Sale sale = new Sale();

            sale.setDiscount(this.setRandomDiscount());
            sale.setCar(this.carService.getRandomCar());

            Customer randomCustomer = this.customerService.getRandomCustomer();

            if (randomCustomer.getId() != i){
                sale.setCustomer(randomCustomer);
                this.saleRepository.saveAndFlush(sale);
            }else {
                i--;
            }
        }
    }

    @Override
    public SaleRootDto getAllSellsWithDiscount() {
        SaleRootDto sDto = new SaleRootDto();

        List<CustomerInfoDto> customerInfoDtos = new ArrayList<>();
        this.saleRepository.findAll().forEach(s ->{
            CarInfoDto carInfoDto = new CarInfoDto();
            CustomerInfoDto cDto = new CustomerInfoDto();

            carInfoDto.setMake(s.getCar().getMake());
            carInfoDto.setModel(s.getCar().getModel());
            carInfoDto.setTravelledDistance(s.getCar().getTravelledDistance());

            cDto.setCar(carInfoDto);
            cDto.setCustomerName(s.getCustomer().getName());
            cDto.setDiscount(s.getDiscount());
            BigDecimal sum = new BigDecimal(0);
            for (Part part : s.getCar().getParts()) {
                sum = sum.add(part.getPrice());
            }
            BigDecimal discount = sum.multiply(BigDecimal.valueOf(s.getDiscount()));

            cDto.setPrice(sum);
            cDto.setPriceWithDiscount(sum.subtract(discount));
            customerInfoDtos.add(cDto);
        });
        sDto.setCustomersList(customerInfoDtos);
        return sDto;
    }

    private Double setRandomDiscount() {
        Random random = new Random();
        Double[] discounts = {0D, 0.05, 0.1, 0.15, 0.2, 0.3, 0.4, 0.5};
        return discounts[random.nextInt(discounts.length)];
    }

}
