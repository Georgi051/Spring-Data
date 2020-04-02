package com.example.cardealer.services.impl;

import com.example.cardealer.domain.entities.Customer;
import com.example.cardealer.domain.entities.Sale;
import com.example.cardealer.reopositories.CarRepository;
import com.example.cardealer.reopositories.SaleRepository;
import com.example.cardealer.services.CarService;
import com.example.cardealer.services.CustomerService;
import com.example.cardealer.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;


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

    private Double setRandomDiscount() {
        Random random = new Random();
        Double[] discounts = {0D, 0.05, 0.1, 0.15, 0.2, 0.3, 0.4, 0.5};
        return discounts[random.nextInt(discounts.length)];
    }

}
