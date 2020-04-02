package com.example.jsonprocessing.services;

import com.example.jsonprocessing.models.dtos.firstQuery.ProductInRangeDto;
import com.example.jsonprocessing.models.dtos.seedData.ProductSeedDto;
import com.example.jsonprocessing.models.dtos.secondQuery.SellerWithLeastOneProductDto;

import java.util.List;

public interface ProductService {
    void seedProducts(ProductSeedDto[] productSeedDto);

    List<ProductInRangeDto> getProductInRange();

    List<SellerWithLeastOneProductDto> getAllProductWithBuyer();
}
