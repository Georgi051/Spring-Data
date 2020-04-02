package com.example.xmlcardealer.services.impl;


import com.example.xmlcardealer.domain.dtos.Query2.ToyotaCarsDto;
import com.example.xmlcardealer.domain.dtos.Query2.ToyotaRootDto;
import com.example.xmlcardealer.domain.dtos.Query4.CarDto;
import com.example.xmlcardealer.domain.dtos.Query4.CarViewDto;
import com.example.xmlcardealer.domain.dtos.Query4.PartDto;
import com.example.xmlcardealer.domain.dtos.Query4.PartsRootDto;
import com.example.xmlcardealer.domain.dtos.seedData.CarSeedDto;
import com.example.xmlcardealer.domain.entities.Car;
import com.example.xmlcardealer.repositories.CarRepository;
import com.example.xmlcardealer.services.CarService;
import com.example.xmlcardealer.services.PartService;
import com.example.xmlcardealer.utils.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PartService partService;
    private final Random random;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, ValidationUtil validationUtil, PartService partService, Random random) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.partService = partService;
        this.random = random;
    }


    @Override
    public void seedCar(List<CarSeedDto> carSeedDto) {
        if (this.carRepository.count() != 0) {
            return;
        }
        carSeedDto.forEach(c -> {
            if (this.validationUtil.isValid(c)) {
                if (this.carRepository
                        .findByMakeAndModelAndTravelledDistance(c.getMake(),
                                c.getModel(), c.getTravelledDistance()) == null) {
                    Car car = this.modelMapper.map(c, Car.class);

                    car.setParts(this.partService.getRandomParts());
                    this.carRepository.saveAndFlush(car);
                } else {
                    System.out.printf("Car %s %s with distance %d is already in data base!%n",
                            c.getMake(), c.getModel(), c.getTravelledDistance());
                }
            } else {
                this.validationUtil.violations(c)
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .forEach(System.out::println);
            }
        });
    }

    @Override
    public Car getRandomCar() {
        long randomId = this.random
                .nextInt((int) this.carRepository.count()) + 1;
        return this.carRepository.getOne(randomId);
    }

    @Override
    public ToyotaRootDto getAllToyotaMake(String make) {
        List<Car> toyotaCars = this.carRepository.findAllByMakeOrderByModelAscTravelledDistanceDesc(make);
        ToyotaRootDto tDto = new ToyotaRootDto();
        List<ToyotaCarsDto> toyotaCarsDtos = toyotaCars.stream().map(c -> this.modelMapper.map(c, ToyotaCarsDto.class)
        ).collect(Collectors.toList());
        tDto.setCars(toyotaCarsDtos);
        return tDto;
    }

    @Override
    public CarViewDto getAllCarsWithTheirParts() {
        CarViewDto carViewDtos = new CarViewDto();

        List<CarDto> carsList = new ArrayList<>();
        this.carRepository.findAll().forEach(car -> {
            PartsRootDto pDto = new PartsRootDto();
            CarDto carDto = this.modelMapper.map(car, CarDto.class);
            List<PartDto> partDtos = car.getParts().stream()
                    .map(p -> this.modelMapper.map(p, PartDto.class)).collect(Collectors.toList());
            pDto.setParts(partDtos);
            carDto.setPartsdto(pDto);
            carsList.add(carDto);

        });
        carViewDtos.setCars(carsList);
        return carViewDtos;
    }
}
