package com.example.jsonprocessing.models.dtos.fourQuery;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class ProductDto {
    @Expose
    private String name;
    @Expose
    private BigDecimal price;


    @NotNull
    @Length(min = 3, message = "Wrong length")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
