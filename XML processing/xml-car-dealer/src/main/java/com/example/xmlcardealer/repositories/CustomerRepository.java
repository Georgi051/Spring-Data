package com.example.xmlcardealer.repositories;

import com.example.xmlcardealer.domain.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByNameAndBirthDate(String name, String birthDate);

    List<Customer> getAllByOrderByBirthDateAscYoungDriver();



}
