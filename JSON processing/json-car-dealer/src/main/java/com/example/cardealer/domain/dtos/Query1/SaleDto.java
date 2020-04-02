package com.example.cardealer.domain.dtos.Query1;

import com.google.gson.annotations.Expose;

public class SaleDto {
    @Expose
    private String car;

    @Expose
    private Long travelledDistance;

    @Expose
    private Double discount;


    public SaleDto() {
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public Long getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(Long travelledDistance) {
        this.travelledDistance = travelledDistance;
    }
}
