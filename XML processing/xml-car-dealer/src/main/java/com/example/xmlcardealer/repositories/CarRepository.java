package com.example.xmlcardealer.repositories;

import com.example.xmlcardealer.domain.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {
    Car findByMakeAndModelAndTravelledDistance(String name, String model, Long distance);

    List<Car> findAllByMakeOrderByModelAscTravelledDistanceDesc(String make);

}
