package com.example.xmlcardealer.services;


import com.example.xmlcardealer.domain.dtos.Query2.ToyotaRootDto;
import com.example.xmlcardealer.domain.dtos.Query4.CarViewDto;
import com.example.xmlcardealer.domain.dtos.seedData.CarSeedDto;
import com.example.xmlcardealer.domain.entities.Car;

import java.util.List;

public interface CarService {

    void seedCar (List<CarSeedDto> carSeedDto);

    Car getRandomCar();

    ToyotaRootDto getAllToyotaMake(String make);

    CarViewDto  getAllCarsWithTheirParts();
}
