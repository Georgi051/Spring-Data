package com.example.cardealer.services;

import com.example.cardealer.domain.dtos.Query2.ToyotaCarsDto;
import com.example.cardealer.domain.dtos.Query4.CarDto;
import com.example.cardealer.domain.dtos.Query4.CarViewDto;
import com.example.cardealer.domain.dtos.seedData.CarSeedDto;
import com.example.cardealer.domain.entities.Car;

import java.util.List;

public interface CarService {

    void seedCar (CarSeedDto[] carSeedDto);

    Car getRandomCar();

    List<ToyotaCarsDto> getAllToyotaMake(String make);

    List<CarViewDto>  getAllCarsWithTheirParts();
}
