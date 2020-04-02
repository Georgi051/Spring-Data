package com.example.cardealer.reopositories;

import com.example.cardealer.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByNameAndBirthDate(String name, String birthDate);

    List<Customer> getAllByOrderByBirthDateAscYoungDriver();



}
