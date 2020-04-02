package com.example.xmlcardealer.services;

import com.example.xmlcardealer.domain.dtos.Query6.SaleRootDto;

public interface SaleService {
    void seedSale();

    SaleRootDto getAllSellsWithDiscount();
}
