package com.example.cardealer.domain.dtos.Query4;



import com.google.gson.annotations.Expose;

import java.util.List;

public class CarViewDto {

    @Expose
    private CarDto car;

    @Expose
    private List<PartDto> parts;

    public CarViewDto() {
    }

    public CarDto getCar() {
        return car;
    }

    public void setCar(CarDto car) {
        this.car = car;
    }

    public List<PartDto> getParts() {
        return parts;
    }

    public void setParts(List<PartDto> parts) {
        this.parts = parts;
    }
}
