package com.example.cardealer.domain.dtos.Query1;

import com.example.cardealer.domain.entities.Sale;
import com.google.gson.annotations.Expose;

import java.util.List;

public class CustomersDto {
    @Expose
    private Long id;
    @Expose
    private String Name;
    @Expose
    private String BirthDate;
    @Expose
    private boolean IsYoungDriver;
    @Expose
    private List<SaleDto> Sales;

    public CustomersDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        this.BirthDate = birthDate;
    }

    public boolean isYoungDriver() {
        return IsYoungDriver;
    }

    public void setYoungDriver(boolean youngDriver) {
        IsYoungDriver = youngDriver;
    }

    public List<SaleDto> getSales() {
        return Sales;
    }

    public void setSales(List<SaleDto> sales) {
        Sales = sales;
    }
}
