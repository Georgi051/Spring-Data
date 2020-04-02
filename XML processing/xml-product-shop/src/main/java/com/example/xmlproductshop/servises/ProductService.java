package com.example.xmlproductshop.servises;

import com.example.xmlproductshop.models.dtos.Query1.ProductsRootDto;
import com.example.xmlproductshop.models.dtos.seedData.ProductSeedDto;

import java.util.List;

public interface ProductService {
    void seedProduct(List<ProductSeedDto> productSeedDtos);

    ProductsRootDto getAllProductsBetween();
}
