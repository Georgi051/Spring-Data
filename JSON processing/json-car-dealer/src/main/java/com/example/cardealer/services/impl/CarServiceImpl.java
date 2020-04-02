package com.example.cardealer.services.impl;

import com.example.cardealer.domain.dtos.Query2.ToyotaCarsDto;
import com.example.cardealer.domain.dtos.Query4.CarDto;
import com.example.cardealer.domain.dtos.Query4.CarViewDto;
import com.example.cardealer.domain.dtos.Query4.PartDto;
import com.example.cardealer.domain.dtos.seedData.CarSeedDto;
import com.example.cardealer.domain.entities.Car;
import com.example.cardealer.reopositories.CarRepository;
import com.example.cardealer.services.CarService;
import com.example.cardealer.services.PartService;
import com.example.cardealer.utils.ValidationUtil;
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
    public void seedCar(CarSeedDto[] carSeedDto) {
        if (this.carRepository.count() != 0){
            return;
        }
        Arrays.stream(carSeedDto).forEach(c -> {
            if (this.validationUtil.isValid(c)){
                if(this.carRepository
                        .findByMakeAndModelAndTravelledDistance(c.getMake(),
                                c.getModel(), c.getTravelledDistance()) == null) {
                    Car car = this.modelMapper.map(c, Car.class);

                    car.setParts(this.partService.getRandomParts());
                    this.carRepository.saveAndFlush(car);
                }else {
                    System.out.printf("Car %s %s with distance %d is already in data base!%n",
                            c.getMake(),c.getModel(),c.getTravelledDistance());
                }
            }else {
                this.validationUtil.getViolation(c)
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
    public List<ToyotaCarsDto> getAllToyotaMake(String make) {
        List<Car> toyotaCars = this.carRepository.findAllByMakeOrderByModelAscTravelledDistanceDesc(make);
        return  toyotaCars.stream().map(c -> this.modelMapper.map(c,ToyotaCarsDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public List<CarViewDto> getAllCarsWithTheirParts() {
        List<CarViewDto> carViewDtos = new ArrayList<>();

          this.carRepository.findAll().forEach(car -> {
              CarViewDto carViewDto = new CarViewDto();

              CarDto carDto = this.modelMapper.map(car,CarDto.class);

              List<PartDto> partDtos = car.getParts().stream()
                            .map(p -> this.modelMapper.map(p,PartDto.class)).collect(Collectors.toList());

              carViewDto.setCar(carDto);
              carViewDto.setParts(partDtos);

              carViewDtos.add(carViewDto);
          });

          return carViewDtos;
    }


}
