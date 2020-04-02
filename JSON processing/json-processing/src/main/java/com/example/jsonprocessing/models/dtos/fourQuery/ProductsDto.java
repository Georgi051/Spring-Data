package com.example.jsonprocessing.models.dtos.fourQuery;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ProductsDto {
    @Expose
    private Integer count;
    @Expose
    private List<ProductDto> products;

    public ProductsDto() {
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
