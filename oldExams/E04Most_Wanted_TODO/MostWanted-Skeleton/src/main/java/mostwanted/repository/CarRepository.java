package mostwanted.repository;

import mostwanted.domain.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
    Car findByBrandAndModelAndYearOfProduction(String brand,String model, Integer yearOfProduction);

}
